package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class ListenerClass implements ContactListener {
    public int count = 0;
    Sound pigSound = Gdx.audio.newSound(Gdx.files.internal("pigSound.mp3"));

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

    @Override
    public void beginContact(Contact contact) {
//        System.out.println("CALLED!!");
        // Get the two fixtures involved in the collision
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if any of the fixtures belong to Wood or Concrete
        //wood concrete
        //handle wood concrete
        if (count!=0){
            handleCollision(fixtureA, fixtureB);
            handleCollision(fixtureB, fixtureA);
        }
        count++;
    }
    public void handleCollision(Fixture fixtureA, Fixture fixtureB) {
//        System.out.println("hh");
//        System.out.println("Fixture A UserData: " + fixtureA.getBody().getUserData());
//        System.out.println("Fixture B UserData: " + fixtureB.getBody().getUserData());
        float posX = fixtureA.getBody().getPosition().x;
        float posY = fixtureA.getBody().getPosition().y;

        float posBX = fixtureB.getBody().getPosition().x;
        float posBY = fixtureB.getBody().getPosition().y;

        // Check if fixtureA belongs to Wood
        if (fixtureA.getBody().getUserData() instanceof Wood && posBX<posX && (posBY>posY
            || fixtureB.getBody().getUserData() instanceof Birds)){
//            System.out.println("hello");
            Wood currWood = (Wood) fixtureA.getBody().getUserData();

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
//                System.out.println("BEFORE WOOD1 "+currWood.currHealth);
                currWood.decreaseHealth(Concrete.GiveDamage);
//                System.out.println("AFTER WOOD1 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood) {
//                System.out.println("BEFORE WOOD2 "+currWood.currHealth);
                currWood.decreaseHealth(Wood.GiveDamage);
//                System.out.println("AFTER WOOD2 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Pigs){
                System.out.println("COCCCCCCCCCCCCCCCCCCCCCCC");
                Pigs currPigs = (Pigs) fixtureB.getBody().getUserData();
                currPigs.decreaseHealth(Wood.GiveDamage);
            }
            else if (fixtureB.getBody().getUserData() instanceof Glass) {
//                System.out.println("BEFORE WOOD2 "+currWood.currHealth);
                currWood.decreaseHealth(Glass.GiveDamage);
//                System.out.println("AFTER WOOD2 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Birds) {
//                System.out.println("BEFORE WOOD3 "+currWood.currHealth);
                Birds currBirds = (Birds) fixtureB.getBody().getUserData();
                currWood.decreaseHealth(currBirds.getDamage());
//                System.out.println("AFTER WOOD3 "+currWood.currHealth);
//                System.out.println(currWood.currHealth);
            }

            if (currWood.currHealth <=0) {
                currWood.destroyMe();
//                System.out.println("WOOD LIST SIZE: "+LevelOne.wood.size());
            }
        }

        // Check if fixtureA belongs to Concrete
        if (fixtureA.getBody().getUserData() instanceof Concrete && posBX<posX && (posBY>posY
            || fixtureB.getBody().getUserData() instanceof Birds)){

            Concrete currConcrete = (Concrete) fixtureA.getBody().getUserData();

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
//                System.out.println("BEFORE CONC1"+currConcrete.currHealth);
                currConcrete.decreaseHealth(Concrete.GiveDamage);
//                System.out.println("AFTER CONC1"+currConcrete.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood){
//                System.out.println("BEFORE CONC2"+currConcrete.currHealth);
                currConcrete.decreaseHealth(Wood.GiveDamage);
//                System.out.println("AFTER CONC2"+currConcrete.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Birds) {
//                System.out.println("BEFORE CONC3"+currConcrete.currHealth);
                Birds currBirds = (Birds) fixtureB.getBody().getUserData();
                currConcrete.decreaseHealth(currBirds.getDamage());
//                System.out.println("AFTER CONC3"+currConcrete.currHealth);
//                System.out.println(currWood.currHealth);
            }
            if (currConcrete.currHealth <=0) {
                currConcrete.destroyMe();
            }
        }

        if (fixtureA.getBody().getUserData() instanceof Glass && posBX<posX && (posBY>posY
            || fixtureB.getBody().getUserData() instanceof Birds)){
//            System.out.println("hello");
            Glass currGlass = (Glass) fixtureA.getBody().getUserData();

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
//                System.out.println("BEFORE WOOD1 "+currWood.currHealth);
                currGlass.decreaseHealth(Concrete.GiveDamage);
//                System.out.println("AFTER WOOD1 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood) {
//                System.out.println("BEFORE WOOD2 "+currWood.currHealth);
                currGlass.decreaseHealth(Wood.GiveDamage);
//                System.out.println("AFTER WOOD2 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Glass) {
//                System.out.println("BEFORE WOOD2 "+currWood.currHealth);
                currGlass.decreaseHealth(Glass.GiveDamage);
//                System.out.println("AFTER WOOD2 "+currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Birds) {
//                System.out.println("BEFORE WOOD3 "+currWood.currHealth);
                Birds currBirds = (Birds) fixtureB.getBody().getUserData();
                currGlass.decreaseHealth(currBirds.getDamage());
//                System.out.println("AFTER WOOD3 "+currWood.currHealth);
//                System.out.println(currWood.currHealth);
            }

            if (currGlass.currHealth <=0) {
                currGlass.destroyMe();
                System.out.println("WOOD LIST SIZE: "+LevelOne.wood.size());
            }
        }

        //check if fixture A belongs to pig
        if (fixtureA.getBody().getUserData() instanceof Pigs &&fixtureB.getBody().getUserData() instanceof Birds){

            Pigs currPigs = (Pigs) fixtureA.getBody().getUserData(); // Retrieve user data

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
                currPigs.decreaseHealth(Concrete.GiveDamage);
                playSound();
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood){
                System.out.println("collision with woodDDDDDD");
                currPigs.decreaseHealth(Wood.GiveDamage);
                System.out.println(currPigs.currHealth);
                playSound();
            }
            else if (fixtureB.getBody().getUserData() instanceof Birds) {
                System.out.println("CCCCollision with Birds");
                Birds currBirds = (Birds) fixtureB.getBody().getUserData();
                currPigs.decreaseHealth(currBirds.getDamage());
                System.out.println(currPigs.currHealth);
                playSound();
//                System.out.println(currWood.currHealth);
            }
            if (currPigs.currHealth <=0) {
                currPigs.destroyMe();
//                System.out.println("S PIG SIZE: "+LevelThree.smallpig.size());
//                System.out.println("L PIG SIZE:"+LevelThree.largepig.size());
            }
        }
    }
    public void playSound(){
        pigSound.play();
    }
};
