package Lab05;
/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

import java.awt.BorderLayout;

import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.loaders.objectfile.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.vecmath.*;

import java.io.*;

public class Lab05HN extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 2;
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
		TransformGroup sceneTH = new TransformGroup();		// for no rotate
		TransformGroup sceneDH = new TransformGroup();		// for the blade
		
		Lab5ShapesHN[] Lab05Shapes = new Lab5ShapesHN[OBJ_NUM];
		Lab05Shapes[0] = new StringShape("HN's Lab05");
		Lab05Shapes[1] = new Test();
		
		Lab5ShapesHN[] Lab05Blade = new Lab5ShapesHN[1];
		Lab05Blade[0] = new Blade();
		
		Lab5ShapesHN[] noRotate = new Lab5ShapesHN[2];		//for the base and xyz
		noRotate[0] = new XYZ(1);
		noRotate[1] = new Base();
		
		sceneTH.addChild(noRotate[0].position_Object());
		sceneTH.addChild(noRotate[1].position_Object());
		sceneDH.addChild(Lab05Blade[0].position_Object());
		for (int i = 0; i < OBJ_NUM; i++)	
			sceneTG.addChild(Lab05Shapes[i].position_Object());
		
		sceneBG.addChild(CommonsHN.add_Lights(CommonsHN.White, 1));	
		sceneBG.addChild(CommonsHN.rotate_Behavior(20000, sceneTG));
		sceneBG.addChild(CommonsHN.rotate_Behavior(20000, sceneDH));
		//sceneBG.addChild(CommonsHN.rotate_Blade(10000, sceneDH));
		sceneBG.addChild(sceneDH);
		sceneBG.addChild(sceneTG);
		sceneBG.addChild(sceneTH);
			
			// make 'sceneTG' continuous rotating
		return sceneBG;
	}
	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Lab05HN(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsHN.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
		
		sceneBG.addChild(CommonsHN.key_Navigation(su));     // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("HN's Lab05");                   // 
		frame.getContentPane().add(new Lab05HN(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
