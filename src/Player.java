import javax.imageio.ImageIO;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private Vector2D Position;
    private Vector2D DeltaPosition = new Vector2D(0,0);  //Vector used for movement. As player moves per frame, vectors for each input get added together and normalized.
    private float Speed;     // Speed while player is moving

    private float dashFactor;
    private float currentDashFactor = 1;
    private long lastDashTime = 0;
    private long dashIntervall;
    private boolean isDashing = false;

    private float dT;

    private final Calc calc = new Calc();

    BufferedImage image; // Read Player Sprite

    {
        try {
            image = ImageIO.read(new File("Textures/Player/char_front.png"));  // Path to Texture
        } catch (IOException e) {
            throw new RuntimeException(e);  // Avoid Errors
        }
    }


    //constructor
    public Player(Vector2D Position, float Speed, float dashFactor, long dashIntervall) { // Constructor
        this.Position = Position;
        this.Speed = Speed;
        this.dashFactor = dashFactor;
        this.dashIntervall = dashIntervall;
    }

    //
    public void updatePosition(float dT){
        Position.setXY(calc.addVec2D(Position , calc.multVec2D (calc.normalize(DeltaPosition), Speed * dT * currentDashFactor))); //Adds DeltaPosition to Position to let player gain movement.
        DeltaPosition.setXY(0,0);    //resets DeltaPosition.
        if (lastDashTime < System.currentTimeMillis() - 40  && isDashing){
            currentDashFactor = 1;
            isDashing = false;
        }
    }

    /** This method determines if an update of the position is even needed. True upon DeltaPos being not (0,0) */
    public boolean isPosUpdateNeeded(){
        return DeltaPosition.is(new Vector2D(0,0));
    }

    public void draw(Graphics2D g2d, Vector2D camPos, float CamDist, Vector2D WindowSize){
        Vector2D distanceVec = calc.subVec2D(Position,camPos);
        Vector2D localPos = calc.divVec2D(distanceVec, CamDist);
        g2d.drawImage(image, (int)(localPos.getX()+(WindowSize.getX()/2) - (10 / CamDist)) , (int)(localPos.getY()+(WindowSize.getY()/2) - (16/ CamDist)),(int)(16 / CamDist) ,(int)(16 / CamDist),null);
    }

    //Move methods:

    public void moveRight(){DeltaPosition = calc.addVec2D(DeltaPosition, new Vector2D(1,0));}
    public void moveLeft(){DeltaPosition = calc.addVec2D(DeltaPosition, new Vector2D(-1,0));}
    public void moveUp(){DeltaPosition = calc.addVec2D(DeltaPosition, new Vector2D(0,-1));}
    public void moveDown(){DeltaPosition = calc.addVec2D(DeltaPosition, new Vector2D(0,1));}

    public void dash(){
        if(lastDashTime < System.currentTimeMillis() - dashIntervall && isPosUpdateNeeded()){
            lastDashTime = System.currentTimeMillis();
            System.out.println(lastDashTime);
            currentDashFactor = dashFactor;

            isDashing = true;
        }
    }

    // Getter and Setter Methods
    public Vector2D getPosition() {
        return Position;
    }

    public void setPosition(float x, float y) {
        Position.setXY(x,y);
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }
}
