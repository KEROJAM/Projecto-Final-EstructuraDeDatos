public class Stack<E> {
    private static final int CAPACITY = 1000;
    private E[] data;
    private int size=0;

    public Stack() {
        data = (E[]) new Object[CAPACITY];
    }

    public void push(E element) {
        if (size == CAPACITY - 1) {
            throw new StackOverflowError("La pila esta llena");
        }
        this.data[this.size] = element;
        this.size++;
    }

    public E pop() throws Exception{
        E result = null;
        if (this.isEmpty()){
            throw  new Exception("La Cola esta vacia");
        }

        result = this.data[0];
        for (int i = 0; i < this.size-1; i++){
            data[i] = data[i + 1];
        }
        this.data[this.size] = null;
        this.size--;
        return result;
    }

    public E peek() throws Exception {
        E result = null;
        if (this.isEmpty()){
            throw new Exception("La Cola esta Vacia");
        }
        result = this.data[0];
        return result;
    }
    public void show() throws Exception {
        if (this.isEmpty()){
            throw new Exception("La Cola esta Vacia");
        }
        for (int i = 0; i < this.size; i++){
            System.out.println(this.data[i]);
        }
    }
    public boolean isEmpty() {
        return this.size == -1;
    }

    public int size() {
        return this.size + 1;
    }
}
