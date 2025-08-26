public class PriorityQueue{

    PriorityNode[] data ; 
    int size;


    public PriorityQueue() {
      data = null;
      size = 10;
    }

    public void pop(){

    }

    public void push(int priority, String value){

        //creamos  nodo a insertar
        PriorityNode nodeToInsert = new PriorityNode(priority, value);
        //aumentamos el tamaño de la cola priorizada

        this.size++;

        //si es el  primero lo ponemos en la posición 1 y listo

        if (size ==1){

            data[size] = nodeToInsert;

        } else {
            //Si no  está vacía la cola priorizada
            //la  posición temporal es la última
            int myPosition = size;
            //ponemos  el nodo ahí
            data[myPosition] = nodeToInsert;
            //encontramos  la posición del padre
            int myParentPosition =(int)(myPosition/2);
            //Y  guardamos el valor del padre
            PriorityNode myParent=  data[myParentPosition];
            //repetimos  intercambiar el padre con el hijo hasta que el padre

            //tenga  mayor prioridad que el hijo

            while (myPosition!=1 &&  myParent.getPriority() > nodeToInsert.getPriority()){
                data[myPosition] = myParent;
                data[myParentPosition] =  nodeToInsert;
                myPosition =myParentPosition;
                myParentPosition = (int)(myParentPosition/2);
                myParent= data[myParentPosition];
            }
        }
    }
}