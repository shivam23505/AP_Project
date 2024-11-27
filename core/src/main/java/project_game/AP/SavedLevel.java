package project_game.AP;
import java.io.*;
import java.util.ArrayList;

public class SavedLevel implements Serializable{
    private static final long serialVersionUID = 1L;
    private int birdCount;
    private boolean level1complete,level2complete,level3complete;
    private ArrayList<SmallPig> smallPigs;
    private ArrayList<MediumPig> mediumPigs;
    private ArrayList<LargePig> largePigs;
    private ArrayList<Wood> wood;
    private ArrayList<Concrete> concrete;
    private ArrayList<Glass>glass;
    private int levelNo;

    //For level1
    public SavedLevel(int birdCount, boolean level1complete, boolean level2complete, boolean level3complete,
                      ArrayList<SmallPig> smallPigs, ArrayList<MediumPig> mediumPigs, ArrayList<LargePig> largePigs,
                      ArrayList<Wood> wood, ArrayList<Concrete> concrete,ArrayList<Glass>glass, int levelNo) {
        this.birdCount = birdCount;
        this.level1complete = level1complete;
        this.level2complete = level2complete;
        this.level3complete = level3complete;
        this.smallPigs = smallPigs;
        this.mediumPigs = mediumPigs;
        this.largePigs = largePigs;
        this.wood = wood;
        this.concrete = concrete;
        this.glass = glass;
        this.levelNo = levelNo;
    }

    public int getBirdCount() {
        return birdCount;
    }

    public ArrayList<Concrete> getConcrete() {
        return concrete;
    }

    public ArrayList<LargePig> getLargePigs() {
        return largePigs;
    }

    public ArrayList<Wood> getWood() {
        return wood;
    }

    public ArrayList<MediumPig> getMediumPigs() {
        return mediumPigs;
    }

    public ArrayList<SmallPig> getSmallPigs() {
        return smallPigs;
    }

    public ArrayList<Glass>getGlass() {return glass;}

    public int getLevelNo(){
        return levelNo;
    }
}
