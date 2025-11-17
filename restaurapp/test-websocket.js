#!/usr/bin/env node

/**
 * Cliente de prueba WebSocket para RestaurApp
 * Verifica que la conexión SockJS/STOMP funcione correctamente
 */

const SockJS = require('sockjs-client');
const Stomp = require('stompjs');

console.log('🔧 Iniciando prueba de WebSocket...\n');

// Configuración
const WEBSOCKET_URL = 'http://localhost:8080/ws';
const TIMEOUT = 10000; // 10 segundos

let connected = false;
let messagesReceived = 0;

// Crear conexión SockJS
console.log(`📡 Conectando a: ${WEBSOCKET_URL}`);
const socket = new SockJS(WEBSOCKET_URL);
const stompClient = Stomp.over(socket);

// Deshabilitar logs de debug (opcional)
// stompClient.debug = null;

// Timeout de seguridad
const timeoutId = setTimeout(() => {
    if (!connected) {
        console.error('\n❌ TIMEOUT: No se pudo conectar en 10 segundos');
        console.log('\n💡 Verifica que:');
        console.log('   1. El backend esté corriendo en el puerto 8080');
        console.log('   2. No haya firewall bloqueando la conexión');
        console.log('   3. La URL sea correcta: http://localhost:8080/ws');
        process.exit(1);
    }
}, TIMEOUT);

// Conectar al servidor
stompClient.connect({}, 
    // Callback de éxito
    function(frame) {
        connected = true;
        clearTimeout(timeoutId);
        
        console.log('✅ CONEXIÓN EXITOSA!\n');
        console.log('📋 Frame de conexión:', frame);
        console.log('\n🔔 Suscribiéndose a topics...\n');
        
        // Suscribirse a /topic/orders
        const subscription1 = stompClient.subscribe('/topic/orders', function(message) {
            messagesReceived++;
            console.log('\n📨 Mensaje recibido en /topic/orders:');
            console.log(message.body);
            try {
                const data = JSON.parse(message.body);
                console.log('📊 Datos parseados:', JSON.stringify(data, null, 2));
            } catch(e) {
                console.log('(No es JSON válido)');
            }
        });
        
        console.log('✅ Suscrito a: /topic/orders');
        
        // Suscribirse a /topic/notifications
        const subscription2 = stompClient.subscribe('/topic/notifications', function(message) {
            messagesReceived++;
            console.log('\n📨 Mensaje recibido en /topic/notifications:');
            console.log(message.body);
        });
        
        console.log('✅ Suscrito a: /topic/notifications');
        
        // Enviar un mensaje de prueba
        console.log('\n📤 Enviando mensaje de prueba...\n');
        
        const testMessage = {
            type: 'TEST',
            message: 'Prueba desde Node.js',
            timestamp: new Date().toISOString()
        };
        
        try {
            stompClient.send('/app/order/status', {}, JSON.stringify(testMessage));
            console.log('✅ Mensaje enviado a /app/order/status');
            console.log('📝 Contenido:', JSON.stringify(testMessage, null, 2));
        } catch(error) {
            console.error('❌ Error al enviar mensaje:', error.message);
        }
        
        // Mantener la conexión abierta por 30 segundos para recibir mensajes
        console.log('\n⏳ Esperando mensajes por 30 segundos...');
        console.log('💡 Crea una orden desde Postman o la aplicación para ver notificaciones en tiempo real\n');
        
        setTimeout(() => {
            console.log('\n📊 RESUMEN DE LA PRUEBA:');
            console.log('─────────────────────────────');
            console.log(`✅ Conexión: EXITOSA`);
            console.log(`📨 Mensajes recibidos: ${messagesReceived}`);
            console.log(`🔔 Suscripciones activas: 2`);
            console.log('─────────────────────────────\n');
            
            if (messagesReceived === 0) {
                console.log('💡 No se recibieron mensajes, pero la conexión funciona.');
                console.log('   Para recibir mensajes, crea una orden desde:');
                console.log('   - Postman (POST /order-ticket)');
                console.log('   - La aplicación Angular');
                console.log('   - O actualiza el estado de una orden (PATCH /order-ticket/{id})\n');
            }
            
            console.log('🔌 Desconectando...');
            stompClient.disconnect(() => {
                console.log('✅ Desconectado correctamente\n');
                process.exit(0);
            });
        }, 30000);
    },
    
    // Callback de error
    function(error) {
        clearTimeout(timeoutId);
        console.error('\n❌ ERROR DE CONEXIÓN:');
        console.error(error);
        console.log('\n💡 Posibles causas:');
        console.log('   1. El backend no está corriendo');
        console.log('   2. El endpoint WebSocket no está configurado correctamente');
        console.log('   3. Problema con CORS o Security');
        console.log('\n🔧 Verifica la configuración en:');
        console.log('   - WebSocketConfig.java');
        console.log('   - WebSecurityConfig.java\n');
        process.exit(1);
    }
);

// Manejar cierre de conexión
socket.onclose = function() {
    if (connected) {
        console.log('\n⚠️  Conexión WebSocket cerrada');
    }
};

// Manejar errores de socket
socket.onerror = function(error) {
    console.error('\n❌ Error en el socket:', error);
};

// Manejar Ctrl+C
process.on('SIGINT', function() {
    console.log('\n\n👋 Interrumpido por el usuario');
    if (stompClient && connected) {
        stompClient.disconnect(() => {
            console.log('✅ Desconectado correctamente\n');
            process.exit(0);
        });
    } else {
        process.exit(0);
    }
});

