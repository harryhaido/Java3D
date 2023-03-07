package ass1;
/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

import java.awt.BorderLayout;

import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.loaders.objectfile.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.vecmath.*;


import java.io.*;

public class Assignment1HN extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 2;
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
		TransformGroup sceneTH = new TransformGroup();
		
		Assignment1ShapesHN[] lab3Shapes = new Assignment1ShapesHN[OBJ_NUM];		//Scene TG
		lab3Shapes[0] = new StringShape("HN's Assignment 1");
		lab3Shapes[1] = new Base();
		
		
		Assignment1ShapesHN[] noRotate = new Assignment1ShapesHN[1];		//three_Axes
		noRotate[0] = new XYZ(1);
		sceneTH.addChild(noRotate[0].position_Object());

		for (int i = 0; i < OBJ_NUM; i++)	
			sceneTG.addChild(lab3Shapes[i].position_Object());
		
		sceneTG.addChild(loadShape());
		sceneBG.addChild(CommonsHN.add_Lights(CommonsHN.White, 1));		//rotating behavior
		sceneBG.addChild(CommonsHN.rotate_Behavior(7500, sceneTG));	
		
		sceneBG.addChild(sceneTG);										//add scene
		sceneBG.addChild(sceneTH);
			
			// make 'sceneTG' continuous rotating
		return sceneBG;
	}
	public static BranchGroup loadShape() {
		int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
		ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI /180.0));
		Scene s = null;
		try {
			s = f.load("images/Ring1.obj");
		} catch (FileNotFoundException e) {			//exception
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s.getSceneGroup();
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Assignment1HN(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsHN.define_Viewer(su, new Point3d(1.0d, 1.0d, 4.0d));
		
		sceneBG.addChild(CommonsHN.key_Navigation(su));     // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("HN's Assignment 1");                   // 
		frame.getContentPane().add(new Assignment1HN(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
