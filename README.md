# Avance Projecto Final 
## Sistema de Banco
Menu 
-Preguntar los 16 digitos del numero de tarjeta

Transferencias 
Ver Movimientos
Realizar pago (Preguntar tdc y cuánto va a depositar) 
Generar NIP

### Pasos de Compilacion
1. Navegar al directorio del proyecto:
    ```bash
   cd /ruta/a/directorio/Projecto-Final-EstrucutraDeDatos/
   ```
2. Compilar todos los archivos Java:
    ```bash
   javac src/*.java
   ```
### Pasos de Ejecucion

1. Ejecutar programa principal
   ```bash
   java -cp src Main
   ```
   
2. Usar el menu:
   ```
   ┌──────────────────────────────────┐
   │       ✦ BANCO FINANCIERO ✦       │
   ├──────────────────────────────────┤
   │ Cliente: *Nombre Cliente*        │
   │ Saldo: *Saldo Cliente*           │
   ├──────────────────────────────────┤
   │ 1. 💰  Realizar depósito         │
   │ 2. 💸  Retirar monto             │
   │ 3. 🔄  Transferir                │
   │ 4. ✅  Realizar Tranferencias    │
   │ 5. 📋  Historial de movimientos  │
   │ 6. 🚪  Cerrar sesión             │
   └──────────────────────────────────┘
   ```

## Guia de Uso


# Características Técnicas

### Estructuras de Datos Implementadas

**Pila (Stack)**
- Operaciones: `push()`, `pop()`, `peek()`, `isEmpty()`, `size()`
- Principio: LIFO (Last In, First Out)
- Uso: Historial de movimientos 

**Cola (Queue)**
- Operaciones: `enqueue()`, `dequeue()`, `peek()`, `isEmpty()`, `size()`
- Principio: FIFO (First In, First Out)
- Uso: Transferencias 

**Lista Enlazada (LinkedList)**
- Soporte para navegación bidireccional
- Métodos de impresión en ambas direcciones
- Base para Stack y Queue

### Manejo de Errores
- Validación de entrada del usuario
- Manejo de pilas/colas vacías
- Mensajes de error en español
