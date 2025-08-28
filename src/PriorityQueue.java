public class PriorityQueue {
    PriorityNode[] data;
    int size;
    int capacity;

    public PriorityQueue() {
        capacity = 10;
        data = new PriorityNode[capacity + 1]; // Index 0 is unused
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(int priority, String value) {
        if (size >= capacity) {
            resize();
        }

        PriorityNode nodeToInsert = new PriorityNode(priority, value);
        size++;
        data[size] = nodeToInsert;

        // Heapify up
        int currentPosition = size;
        while (currentPosition > 1) {
            int parentPosition = currentPosition / 2;
            if (data[parentPosition].getPriority() > data[currentPosition].getPriority()) {
                swap(parentPosition, currentPosition);
                currentPosition = parentPosition;
            } else {
                break;
            }
        }
    }

    public PriorityNode pop() {
        if (isEmpty()) {
            return null;
        }

        PriorityNode result = data[1];
        data[1] = data[size];
        data[size] = null;
        size--;

        // Heapify down
        int currentPosition = 1;
        while (currentPosition * 2 <= size) {
            int leftChild = currentPosition * 2;
            int rightChild = currentPosition * 2 + 1;
            int smallestChild = leftChild;

            if (rightChild <= size && data[rightChild].getPriority() < data[leftChild].getPriority()) {
                smallestChild = rightChild;
            }

            if (data[currentPosition].getPriority() > data[smallestChild].getPriority()) {
                swap(currentPosition, smallestChild);
                currentPosition = smallestChild;
            } else {
                break;
            }
        }

        return result;
    }

    private void swap(int i, int j) {
        PriorityNode temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private void resize() {
        capacity *= 2;
        PriorityNode[] newData = new PriorityNode[capacity + 1];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    public void clear() {
        data = new PriorityNode[capacity + 1];
        size = 0;
    }
}