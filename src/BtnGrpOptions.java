public enum BtnGrpOptions {
    YES, NO, NA;

    @Override
    public String toString(){
        return switch(this){
            case YES -> "Yes";
            case NO -> "No";
            case NA -> "I don't mind";
        };
    }
}
