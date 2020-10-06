import processing.core.*;
import processing.data.Table;
import processing.data.TableRow;

public class main  extends PApplet {
    public static void main(String[] args) { PApplet.main("main"); }
    float angle;

    Table table;
    float r = 200;

    PImage earth;
    PShape globe;

    @Override
    public void settings() {
        size(600, 600,P3D);
    }

    @Override
    public void setup() {
        earth = loadImage("earth.jpg");
        // table = loadTable("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.csv", "header");
        table = loadTable("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.csv", "header");

        noStroke();
        globe = createShape(SPHERE, r);
        globe.setTexture(earth);
    }

    @Override
    public void draw() {
        background(51);

       translate((float) (width*0.5), (float)(height*0.5));
        rotateY(angle);
        angle += 0.05;

        lights();
        fill(200);
        noStroke();
        //sphere(r);
        shape(globe);

        for (TableRow row : table.rows()) {
            float lat = row.getFloat("latitude");
            float lon = row.getFloat("longitude");
            float mag = row.getFloat("mag");

            // original version
            // float theta = radians(lat) + PI/2;

            // fix: no + PI/2 needed, since latitude is between -180 and 180 deg
            float theta = radians(lat);

            float phi = radians(lon) + PI;

            // original version
            // float x = r * sin(theta) * cos(phi);
            // float y = -r * sin(theta) * sin(phi);
            // float z = r * cos(theta);

            // fix: in OpenGL, y & z axes are flipped from math notation of spherical coordinates
            float x = r * cos(theta) * cos(phi);
            float y = -r * sin(theta);
            float z = -r * cos(theta) * sin(phi);

            PVector pos = new PVector(x, y, z);

            float h = pow(10, mag);
            float maxh = pow(10, 7);
            h = map(h, 0, maxh, 10, 100);
            PVector xaxis = new PVector(1, 0, 0);
            float angleb = PVector.angleBetween(xaxis, pos);
            PVector raxis = xaxis.cross(pos);



            pushMatrix();
            translate(x, y, z);
            rotate(angleb, raxis.x, raxis.y, raxis.z);
            fill(255);
            box(h, 5, 5);
            popMatrix();
        }
    }


}


