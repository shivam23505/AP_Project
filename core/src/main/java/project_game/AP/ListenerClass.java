package project_game.AP;

import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class ListenerClass implements ContactListener {

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
        System.out.println("CALLED!!");
        // Get the two fixtures involved in the collision
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if any of the fixtures belong to Wood or Concrete
        //wood concrete
        //handle wood concrete
        handleCollision(fixtureA, fixtureB);
        handleCollision(fixtureB, fixtureA);
    }
    public void handleCollision(Fixture fixtureA, Fixture fixtureB) {
        System.out.println("hh");
        System.out.println("Fixture A UserData: " + fixtureA.getBody().getUserData());
        System.out.println("Fixture B UserData: " + fixtureB.getBody().getUserData());
        float posX = fixtureA.getBody().getPosition().x;
        float posY = fixtureA.getBody().getPosition().y;

        float posBX = fixtureB.getBody().getPosition().x;
        float posBY = fixtureB.getBody().getPosition().y;

        // Check if fixtureA belongs to Wood
        if (fixtureA.getBody().getUserData() instanceof Wood && posBX<posX && (posBY>posY /*Fixture B is a bird*/)){
            System.out.println("hello");
            Wood currWood = (Wood) fixtureA.getBody().getUserData();

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
                currWood.decreaseHealth(Concrete.GiveDamage);
                System.out.println(currWood.currHealth);
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood){
                currWood.decreaseHealth(Wood.GiveDamage);
            }
            else{
                currWood.decreaseHealth(1);
                System.out.println(currWood.currHealth);
            }

            if (currWood.currHealth <=0) {
                currWood.destroyMe();
            }
        }

        // Check if fixtureA belongs to Concrete
        if (fixtureA.getBody().getUserData() instanceof Concrete && posBX<posX && posBY>posY) {
            System.out.println("wello");
            Concrete currConcrete = (Concrete) fixtureA.getBody().getUserData();

            if (fixtureB.getBody().getUserData() instanceof Concrete) {
                currConcrete.decreaseHealth(Concrete.GiveDamage);
            }
            else if (fixtureB.getBody().getUserData() instanceof Wood){
                currConcrete.decreaseHealth(Wood.GiveDamage);
            }
            else{
                currConcrete.decreaseHealth(1);
            }

            if (currConcrete.currHealth <=0) {
                currConcrete.destroyMe();
            }
        }
    }
};
