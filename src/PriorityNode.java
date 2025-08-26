public class PriorityNode<dataType> {

    private int priority;

    private dataType data;

    

    //constructors

    public PriorityNode() {

    }    

    public PriorityNode(int priority, dataType data) {

      this.priority  = priority;

      this.data  = data;

      }    

    //setters &  getters

    public int getPriority() {

      return priority;

      }    

    public void setPriority(int  priority) {

      this.priority  = priority;

      }    

    public dataType getData() {

      return data;

      }    

    public void setData(dataType data) {

      this.data  = data;

      }    

    @Override

    public String toString() {

      return "ProrityNode{" + "priority="  + priority + ", data=" + data + '}';

      }
      

      }     