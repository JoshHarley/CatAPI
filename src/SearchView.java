import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SearchView {
    private JPanel searchViewPanel;
    private BtnGrpOptions lap, indoor, hairless, rare, rex, tail, legs, hypoallergenic;

    public SearchView(){
        this.searchViewPanel = new JPanel();
        this.lap = BtnGrpOptions.NA;
        this.indoor = BtnGrpOptions.NA;
        this.hairless = BtnGrpOptions.NA;
        this.rare = BtnGrpOptions.NA;
        this.rex = BtnGrpOptions.NA;
        this.tail = BtnGrpOptions.NA;
        this.legs = BtnGrpOptions.NA;
        this.hypoallergenic = BtnGrpOptions.NA;
    }

    public JPanel yesNoMaybeButtonGroup(CatFilter filter){
        JPanel toReturn = new JPanel();
        toReturn.setBorder(BorderFactory.createTitledBorder(filter.toString()));
        JRadioButton yesBtn = new JRadioButton(BtnGrpOptions.YES.toString());
        JRadioButton noBtn = new JRadioButton(BtnGrpOptions.NO.toString());
        JRadioButton NABtn = new JRadioButton(BtnGrpOptions.NA.toString());
        ButtonGroup btnGrp = new ButtonGroup();
        btnGrp.add(yesBtn);
        btnGrp.add(noBtn);
        btnGrp.add(NABtn);
        NABtn.setSelected(true);
        toReturn.add(yesBtn);
        toReturn.add(noBtn);
        toReturn.add(NABtn);
        ActionListener actionListener = e -> {
            if(filter == CatFilter.LAP){
                this.lap = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.INDOOR){
                this.indoor = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.HAIRLESS){
                this.hairless = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.RARE){
                this.rare = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.REX){
                this.rex = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.SUPPRESSED_TAIL){
                this.tail = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.SHORT_LEGS){
                this.legs = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            } else if (filter == CatFilter.HYPOALLERGENIC){
                this.hypoallergenic = BtnGrpOptions.valueOf(btnGrp.getSelection().getActionCommand());
            }
        };
        yesBtn.addActionListener(actionListener);
        noBtn.addActionListener(actionListener);
        NABtn.addActionListener(actionListener);
        toReturn.setBounds(new Rectangle());
        return toReturn;
    }
}
