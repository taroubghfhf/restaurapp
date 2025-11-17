import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WebSocketService, NotificationService } from '../../../services';
import { Subscription } from 'rxjs';

interface WebSocketMessage {
  timestamp: Date;
  type: 'received' | 'sent' | 'system';
  data: any;
  raw?: string;
}

interface WebSocketConfig {
  host: string;
  port: number;
  path: string;
  protocol: 'ws' | 'wss';
}

@Component({
  selector: 'app-websocket-monitor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './websocket-monitor.component.html',
  styleUrl: './websocket-monitor.component.scss'
})
export class WebSocketMonitorComponent implements OnInit, OnDestroy {
  private webSocketService = inject(WebSocketService);
  private notificationService = inject(NotificationService);
  private messageSubscription?: Subscription;

  // Configuración del WebSocket
  config = signal<WebSocketConfig>({
    host: '3.16.154.248',
    port: 8080,
    path: '/ws',
    protocol: 'ws'
  });

  // Estado
  isConnected = signal(false);
  isConnecting = signal(false);
  messages = signal<WebSocketMessage[]>([]);
  autoScroll = signal(true);
  maxMessages = signal(100);

  // Mensaje de prueba
  testMessage = signal('');

  // Estadísticas
  stats = signal({
    totalMessages: 0,
    connected: false,
    connectedAt: null as Date | null,
    lastMessageAt: null as Date | null
  });

  ngOnInit(): void {
    this.loadConfig();
    this.checkConnectionStatus();
    this.subscribeToMessages();
  }

  ngOnDestroy(): void {
    this.messageSubscription?.unsubscribe();
  }

  /**
   * Carga la configuración guardada
   */
  loadConfig(): void {
    const savedConfig = localStorage.getItem('websocket_config');
    if (savedConfig) {
      try {
        const config = JSON.parse(savedConfig);
        this.config.set(config);
      } catch (error) {
        console.error('Error al cargar configuración:', error);
      }
    }
  }

  /**
   * Guarda la configuración
   */
  saveConfig(): void {
    localStorage.setItem('websocket_config', JSON.stringify(this.config()));
    this.notificationService.success('Configuración guardada');
  }

  /**
   * Verifica el estado de conexión
   */
  checkConnectionStatus(): void {
    const connected = this.webSocketService.isConnected();
    this.isConnected.set(connected);
    
    if (connected) {
      this.stats.update(s => ({ ...s, connected: true }));
    }
  }

  /**
   * Se suscribe a los mensajes del WebSocket
   */
  subscribeToMessages(): void {
    this.messageSubscription = this.webSocketService.messages$.subscribe({
      next: (data) => {
        this.addMessage('received', data);
        this.stats.update(s => ({
          ...s,
          totalMessages: s.totalMessages + 1,
          lastMessageAt: new Date()
        }));
      },
      error: (error) => {
        console.error('Error en WebSocket:', error);
        this.addSystemMessage('Error: ' + error.message, 'error');
      }
    });
  }

  /**
   * Conecta al WebSocket
   */
  connect(): void {
    if (this.isConnected()) {
      this.notificationService.warning('Ya está conectado al WebSocket');
      return;
    }

    this.isConnecting.set(true);
    const cfg = this.config();
    const url = `${cfg.protocol}://${cfg.host}:${cfg.port}${cfg.path}`;

    this.addSystemMessage(`Conectando a ${url}...`, 'info');

    try {
      this.webSocketService.connect(url);
      
      // Esperar un momento para verificar la conexión
      setTimeout(() => {
        const connected = this.webSocketService.isConnected();
        this.isConnected.set(connected);
        this.isConnecting.set(false);

        if (connected) {
          this.stats.update(s => ({
            ...s,
            connected: true,
            connectedAt: new Date()
          }));
          this.addSystemMessage('✅ Conectado exitosamente', 'success');
          this.notificationService.success('WebSocket conectado');
        } else {
          this.addSystemMessage('❌ No se pudo conectar', 'error');
          this.notificationService.error('No se pudo conectar al WebSocket');
        }
      }, 1000);
    } catch (error: any) {
      this.isConnecting.set(false);
      this.addSystemMessage('❌ Error: ' + error.message, 'error');
      this.notificationService.error('Error al conectar al WebSocket');
    }
  }

  /**
   * Desconecta del WebSocket
   */
  disconnect(): void {
    if (!this.isConnected()) {
      this.notificationService.warning('No hay conexión activa');
      return;
    }

    this.webSocketService.disconnect();
    this.isConnected.set(false);
    this.stats.update(s => ({
      ...s,
      connected: false,
      connectedAt: null
    }));
    
    this.addSystemMessage('🔌 Desconectado', 'info');
    this.notificationService.info('WebSocket desconectado');
  }

  /**
   * Envía un mensaje de prueba
   */
  sendTestMessage(): void {
    const message = this.testMessage().trim();
    
    if (!message) {
      this.notificationService.warning('Escribe un mensaje para enviar');
      return;
    }

    if (!this.isConnected()) {
      this.notificationService.error('No hay conexión activa');
      return;
    }

    try {
      // Intentar parsear como JSON
      let data: any;
      try {
        data = JSON.parse(message);
      } catch {
        data = message;
      }

      // Enviar al destino por defecto /app/order/status
      this.webSocketService.send(data, '/app/order/status');
      this.addMessage('sent', data, message);
      this.testMessage.set('');
      this.notificationService.success('Mensaje enviado');
    } catch (error: any) {
      this.addSystemMessage('❌ Error al enviar: ' + error.message, 'error');
      this.notificationService.error('Error al enviar mensaje');
    }
  }

  /**
   * Agrega un mensaje al log
   */
  addMessage(type: 'received' | 'sent', data: any, raw?: string): void {
    const message: WebSocketMessage = {
      timestamp: new Date(),
      type,
      data,
      raw
    };

    this.messages.update(msgs => {
      const newMessages = [...msgs, message];
      // Limitar el número de mensajes
      if (newMessages.length > this.maxMessages()) {
        return newMessages.slice(-this.maxMessages());
      }
      return newMessages;
    });

    if (this.autoScroll()) {
      this.scrollToBottom();
    }
  }

  /**
   * Agrega un mensaje del sistema
   */
  addSystemMessage(text: string, level: 'info' | 'success' | 'error'): void {
    const message: WebSocketMessage = {
      timestamp: new Date(),
      type: 'system',
      data: { text, level }
    };

    this.messages.update(msgs => [...msgs, message]);

    if (this.autoScroll()) {
      this.scrollToBottom();
    }
  }

  /**
   * Limpia el log de mensajes
   */
  clearMessages(): void {
    this.messages.set([]);
    this.notificationService.info('Log limpiado');
  }

  /**
   * Resetea las estadísticas
   */
  resetStats(): void {
    this.stats.set({
      totalMessages: 0,
      connected: this.isConnected(),
      connectedAt: this.isConnected() ? new Date() : null,
      lastMessageAt: null
    });
    this.notificationService.info('Estadísticas reseteadas');
  }

  /**
   * Actualiza la configuración
   */
  updateConfig(field: keyof WebSocketConfig, value: any): void {
    this.config.update(cfg => ({ ...cfg, [field]: value }));
  }

  /**
   * Hace scroll al final del log
   */
  scrollToBottom(): void {
    setTimeout(() => {
      const logElement = document.querySelector('.messages-log');
      if (logElement) {
        logElement.scrollTop = logElement.scrollHeight;
      }
    }, 10);
  }

  /**
   * Formatea un objeto JSON para mostrar
   */
  formatJson(obj: any): string {
    try {
      return JSON.stringify(obj, null, 2);
    } catch {
      return String(obj);
    }
  }

  /**
   * Formatea la fecha
   */
  formatTime(date: Date): string {
    return new Intl.DateTimeFormat('es-CO', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      fractionalSecondDigits: 3
    }).format(date);
  }

  /**
   * Formatea la duración de conexión
   */
  getConnectionDuration(): string {
    const connectedAt = this.stats().connectedAt;
    if (!connectedAt) return '-';

    const duration = Date.now() - connectedAt.getTime();
    const seconds = Math.floor(duration / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);

    if (hours > 0) {
      return `${hours}h ${minutes % 60}m ${seconds % 60}s`;
    } else if (minutes > 0) {
      return `${minutes}m ${seconds % 60}s`;
    } else {
      return `${seconds}s`;
    }
  }

  /**
   * Carga mensajes de prueba
   */
  loadExampleMessage(type: string): void {
    const examples: { [key: string]: any } = {
      order: {
        type: 'NEW_ORDER',
        orderId: 123,
        tableId: 5,
        items: [
          { productId: 1, name: 'Pizza Margarita', quantity: 2 }
        ]
      },
      status: {
        type: 'ORDER_STATUS_UPDATE',
        orderId: 123,
        status: 'PREPARING'
      },
      notification: {
        type: 'NOTIFICATION',
        message: 'Nueva orden en mesa 5',
        priority: 'HIGH'
      }
    };

    this.testMessage.set(JSON.stringify(examples[type], null, 2));
  }
}



