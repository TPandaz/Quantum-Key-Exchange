public class Qubit{
    private int value;
    private int polarization;

    //constructor which inits random value(0,1) for value and polarization 
    public Qubit(){
        this.value = (int)Math.round(Math.random());
        this.polarization = (int)Math.round(Math.random());
    }

    //constructor which substitutes the new method in the Qubit class diagram
    public Qubit(int value, int polarization){
        this.value = value;
        this.polarization = polarization;
    }


    public void set(int value, int polarization){
        this.value = value;
        this.polarization = polarization;
    }

    public int measure(int polarization){
        if(polarization == this.polarization){
            return this.value;
        }else{
            int value = (int)Math.round(Math.random());
            //set(value, polarization);
            return value;
        }
    }

    //getters
    public int getValue(){
        return this.value;
    }

    public int getPolarization(){
        return this.polarization;
    }
}