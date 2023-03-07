package Lab05;
/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

import java.awt.Font;


import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;

import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public abstract class Lab5ShapesHN {
	protected abstract Node create_Object();	           // use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
}

class Test extends Lab5ShapesHN	{
	public BranchGroup PrimitiveShape() {
		float a = 0f;
		float b = 0;
		float c = 0;
		Vector3f[] position = {new Vector3f(a, b, c), new Vector3f(a, b + 0.5f, c), new Vector3f(a+0.16f, b + 0.67f, c)};
		BranchGroup objBG = new BranchGroup();
		
		Appearance app1 = CommonsHN.obj_Appearance(CommonsHN.Orange);
		Appearance app2 = CommonsHN.obj_Appearance(CommonsHN.Red);
		Appearance app3 = CommonsHN.obj_Appearance(CommonsHN.Blue);
		
		Primitive[] priShape = new Primitive[3];
		priShape[1] = new Sphere(0.12f, Primitive.GENERATE_NORMALS, 60, app2);
		priShape[0] = new Cylinder(0.12f, 1.0f, Primitive.GENERATE_NORMALS, 30, 30, app1);
		priShape[2] = new Box(0.26f, 0.06f, 0.12f, app3);

		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		for (int i = 0; i < 3; i++) {
			trans3d.setTranslation(position[i]);
			trans = new TransformGroup(trans3d);
			objBG.addChild(trans);
			trans.addChild(priShape[i]);
		}
		return objBG;
	}
	private TransformGroup objTG;                              // use 'objTG' to position an object
	public Test()	{
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(1);                               
		objTG = new TransformGroup(scaler);
		objTG.addChild(create_Object());		   
	}
	protected Node create_Object()	{
		return PrimitiveShape();
	}
	public Node position_Object() {
		return objTG;
	}
	
}

class Base extends Lab5ShapesHN	{
	public static Texture texturedApp(String name) {
		String filename ="images/" + name + ".jpg";		//open file
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();
		if (image == null)
		System.out.println("Cannot load file: " + filename);
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		return texture;
		}
		public Box textureBox() {
			Appearance app = new Appearance();
			app.setTexture(texturedApp("dirt"));
			TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.5f);	//set atribute
			app.setTransparencyAttributes(ta);
			Box textured_box = new Box(0.5f,0.04f,0.5f, Primitive.GENERATE_TEXTURE_COORDS, app);		//create box
		return textured_box;
		}
		private TransformGroup objTG;                              // use 'objTG' to position an object      
		public Base()	{
			
			Transform3D scaler = new Transform3D();
			scaler.setScale(1);                              // scaling 
			Transform3D translation = new Transform3D();           // translation
			translation.setTranslation(new Vector3f(0f, -0.46f, 0f));
			objTG = new TransformGroup(translation);
			objTG.addChild(create_Object());		   
		}
		protected Node create_Object()	{
			return textureBox();
		}
		public Node position_Object() {
			return objTG;
		}
}
class Blade extends Lab5ShapesHN	{
	public BranchGroup BladeShape() {
		float a = 0f;
		float b = 0;
		float c = 0;
		Vector3f[] position = {new Vector3f(a-0.06f, b, c), new Vector3f(a, b, c)};
		BranchGroup objBG = new BranchGroup();
		
		Appearance app1 = CommonsHN.obj_Appearance(CommonsHN.Red);
		Appearance app2 = CommonsHN.obj_Appearance(CommonsHN.Magenta);
	
		Primitive[] priShape = new Primitive[3];
		priShape[0] = new Sphere(0.06f, Primitive.GENERATE_NORMALS, 60, app1);
		priShape[1] = new Box(0.01f, 0.06f, 0.5f, app2);

		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		for (int i = 0; i < 2; i++) {
			trans3d.setTranslation(position[i]);
			trans = new TransformGroup(trans3d);
			objBG.addChild(trans);
			trans.addChild(priShape[i]);
		}
		
		objBG.addChild(rotate_Blade(5000, trans ));
		return objBG;
	}
	public final static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
	private TransformGroup objTG;                              // use 'objTG' to position an object      
	public static RotationInterpolator rotate_Blade(int r_num, TransformGroup rotTG) {
		rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D xAxis = new Transform3D();
		xAxis.rotZ(-Math.PI/2);
		Alpha rotationAlpha = new Alpha(-1, r_num);
		RotationInterpolator rot_beh = new RotationInterpolator(rotationAlpha, rotTG, xAxis, 0.0f, (float) Math.PI * 2.0f);
		rot_beh.setSchedulingBounds(hundredBS);
		return rot_beh;
	}
	public Blade()	{
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(1);                              // scaling 4x4 matrix 
		Transform3D translation = new Transform3D();           // 4x4 matrix for rotation
		translation.setTranslation(new Vector3f(0.48f, 0.67f, 0f));
       
		objTG = new TransformGroup(translation);
		objTG.addChild(create_Object()); 	
		
	}
	protected Node create_Object()	{
		return BladeShape();
	}
	public Node position_Object() {
		return objTG;
	}
}

class XYZ extends Lab5ShapesHN	{
	int si;
	public XYZ(int n)	{				//length of XYZ
		si = n;
	}
	protected Node create_Object()	{
		Point3f coor[] = new Point3f[4];                   // declare 4 points for XYZ
		LineArray lineArr = new LineArray(8, LineArray.COLOR_3 | LineArray.COORDINATES);
		coor[0] = new Point3f(0, 0, 0); 
		coor[1] = new Point3f(si, 0, 0); 				//Position 1
		coor[2] = new Point3f(0, si, 0); 				//Position 2
		coor[3] = new Point3f(0, 0, si); 				//Position 3
		
			lineArr.setCoordinate(0, coor[0]);         // define point pairs for each line
			lineArr.setColor(0, CommonsHN.Red);        // specify color for each pair of points
			lineArr.setCoordinate(1, coor[1]);         // define point pairs for each line
			lineArr.setColor(1, CommonsHN.Red);        // specify color for each pair of points
			lineArr.setCoordinate(2, coor[0]);         // define point pairs for each line
			lineArr.setColor(2, CommonsHN.Green);        // specify color for each pair of points
			lineArr.setCoordinate(3, coor[2]);         // define point pairs for each line
			lineArr.setColor(3, CommonsHN.Green);        // specify color for each pair of points
			lineArr.setCoordinate(4, coor[0]);         // define point pairs for each line
			lineArr.setColor(4, CommonsHN.Blue);        // specify color for each pair of points
			lineArr.setCoordinate(5, coor[3]);         // define point pairs for each line
			lineArr.setColor(5, CommonsHN.Blue);        // specify color for each pair of points
		return new Shape3D(lineArr);                        // create and return a Shape3D
	}
	public Node position_Object()	{
		return create_Object();
	}
}


class StringShape extends Lab5ShapesHN {
	private TransformGroup objTG;                              // use 'objTG' to position an object
	private String str;
	public StringShape(String str_ltrs) {
		str = str_ltrs;		
		
		Transform3D translation = new Transform3D();           // 4x4 matrix for rotation
		translation.setTranslation(new Vector3f(0.17f, 0.64f, 0f));
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.1);                              // scaling 4x4 matrix 
		
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotY(Math.PI);
		
		
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		
		trfm.mul(translation);
		trfm.mul(scaler);                              // apply scaler first
		trfm.mul(rotator);                                 // apply rotation next
		
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());		   // apply scaling to change the string's size
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);		
		Appearance app = CommonsHN.obj_Appearance(CommonsHN.White);
		Point3f pos = new Point3f(-str.length()/4f, 0.0f, 1.1f);// position for the string 
		Text3D text3D = new Text3D(font3D, str, pos);      // create a text3D object
		return new Shape3D(text3D, app);
	}
	public Node position_Object() {
		return objTG;
	}
}
