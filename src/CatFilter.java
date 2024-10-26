import java.util.Map;

public enum CatFilter {
    ORIGIN, INDOOR, LAP, ADAPTABILITY, AFFECTION, CHILD, DOG, ENERGY, GROOMING, SHEDDING, STRANGER, VOCAL,
    HAIRLESS, RARE, REX, SUPPRESSED_TAIL, SHORT_LEGS, HYPOALLERGENIC, INTELLIGENCE, SOCIAL, HEALTH;

    @Override
    public String toString(){
        return switch(this){
            case ORIGIN -> "Country of Origin";
            case INDOOR -> "Indoor Cat";
            case LAP -> "Lap Cat";
            case ADAPTABILITY -> "Adaptability";
            case AFFECTION -> "Affection Level";
            case CHILD -> "Child Friendly";
            case DOG -> "Dog Friendly";
            case ENERGY -> "Energy Levels";
            case GROOMING -> "Grooming";
            case SHEDDING -> "Shedding Level";
            case STRANGER -> "Stranger Friendly";
            case VOCAL -> "Vocalisation";
            case HAIRLESS -> "Hairless";
            case RARE -> "Rare";
            case REX -> "Rex";
            case SUPPRESSED_TAIL -> "Suppressed Tail";
            case SHORT_LEGS -> "Short Legs";
            case HYPOALLERGENIC -> "Hypoallergenic";
            case INTELLIGENCE -> "Intelligence Level";
            case SOCIAL -> "Social Needs";
            case HEALTH -> "Health Issues";
        };
    }

}
