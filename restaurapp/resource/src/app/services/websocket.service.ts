import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

/**
 * Servicio para manejar WebSocket con SockJS y STOMP
 * Para usar con el backend de Spring Boot
 */
@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client | null = null;
  private messageSubject = new Subject<any>();
  private connected = false;

  /**
   * Observable para recibir mensajes del WebSocket
   */
  get messages$(): Observable<any> {
    return this.messageSubject.asObservable();
  }

  /**
   * Conecta al servidor WebSocket usando SockJS/STOMP
   * @param url URL base del servidor (ej: http://localhost:8080/ws)
   */
  connect(url: string): void {
    if (this.connected) {
      console.warn('WebSocket ya está conectado');
      return;
    }

    try {
      // Convertir ws:// a http:// para SockJS
      const httpUrl = url.replace('ws://', 'http://').replace('wss://', 'https://');
      
      this.stompClient = new Client({
        webSocketFactory: () => new SockJS(httpUrl) as any,
        
        connectHeaders: {},
        
        debug: (str) => {
          console.log('STOMP Debug:', str);
        },
        
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        
        onConnect: (frame) => {
          console.log('STOMP conectado:', frame);
          this.connected = true;
          
          // Suscribirse a los topics principales
          this.subscribeToTopics();
        },
        
        onStompError: (frame) => {
          console.error('Error STOMP:', frame);
          this.connected = false;
        },
        
        onWebSocketClose: (event) => {
          console.log('WebSocket cerrado:', event);
          this.connected = false;
        }
      });

      this.stompClient.activate();
    } catch (error) {
      console.error('Error al conectar WebSocket:', error);
      this.connected = false;
    }
  }

  /**
   * Se suscribe a los topics del servidor
   */
  private subscribeToTopics(): void {
    if (!this.stompClient || !this.connected) {
      return;
    }

    // Suscribirse al topic general de órdenes
    this.stompClient.subscribe('/topic/orders', (message: IMessage) => {
      console.log('Mensaje recibido en /topic/orders:', message);
      this.handleMessage(message);
    });

    // Suscribirse a notificaciones generales
    this.stompClient.subscribe('/topic/notifications', (message: IMessage) => {
      console.log('Mensaje recibido en /topic/notifications:', message);
      this.handleMessage(message);
    });

    console.log('Suscripciones completadas');
  }

  /**
   * Maneja los mensajes recibidos
   */
  private handleMessage(message: IMessage): void {
    try {
      const data = JSON.parse(message.body);
      this.messageSubject.next(data);
    } catch (error) {
      console.error('Error al parsear mensaje:', error);
      // Si no es JSON, enviar el texto plano
      this.messageSubject.next(message.body);
    }
  }

  /**
   * Envía un mensaje a través del WebSocket
   * @param destination Destino STOMP (ej: /app/order/status)
   * @param message Mensaje a enviar
   */
  send(message: any, destination: string = '/app/order/status'): void {
    if (!this.connected || !this.stompClient) {
      console.error('WebSocket no está conectado');
      return;
    }

    try {
      const body = typeof message === 'string' ? message : JSON.stringify(message);
      this.stompClient.publish({
        destination: destination,
        body: body
      });
      console.log('Mensaje enviado a', destination, ':', body);
    } catch (error) {
      console.error('Error al enviar mensaje:', error);
    }
  }

  /**
   * Cierra la conexión WebSocket
   */
  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
      this.connected = false;
      console.log('WebSocket desconectado');
    }
  }

  /**
   * Verifica si el WebSocket está conectado
   */
  isConnected(): boolean {
    return this.connected && this.stompClient !== null;
  }

  /**
   * Suscribe a un topic específico
   * @param topic Topic al que suscribirse (ej: /topic/orders/123)
   * @param callback Función que se ejecuta al recibir mensajes
   */
  subscribe(topic: string, callback: (message: any) => void): void {
    if (!this.stompClient || !this.connected) {
      console.error('No se puede suscribir, WebSocket no está conectado');
      return;
    }

    this.stompClient.subscribe(topic, (message: IMessage) => {
      try {
        const data = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        callback(message.body);
      }
    });
  }
}

