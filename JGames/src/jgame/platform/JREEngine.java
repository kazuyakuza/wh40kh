package jgame.platform;
import jgame.impl.*;
import jgame.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.awt.image.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import java.awt.event.*;

/** Basic engine functionality for JRE platforms: input handling, audio, window, misc. */
@SuppressWarnings("unused")
class JREEngine
implements MouseListener, MouseMotionListener, FocusListener,
KeyListener, WindowListener {

	public JREEngine(EngineLogic el, JGEngineInterface eng) {
		this.el = el;
		this.eng = eng;
	}

	void updateMouse(MouseEvent e,boolean pressed, boolean released,
	boolean inside) {
		mousepos = e.getPoint();
		mousepos.x = (int)((mousepos.x/*-el.canvas_xofs*/)/el.x_scale_fac);
		mousepos.y = (int)((mousepos.y/*-el.canvas_yofs*/)/el.y_scale_fac);
		mouseinside=inside;
		int button=0;
		if ((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0) button=1;
		if ((e.getModifiers()&InputEvent.BUTTON2_MASK)!=0) button=2;
		if ((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0) button=3;
		if (button==0) return;
		if (pressed)  {
			mousebutton[button]=true;
			keymap[255+button]=true;
			if (wakeup_key==-1 || wakeup_key==255+button) {
				if (!eng.isRunning()) {
					eng.start();
					// mouse button is cleared when it is used as wakeup key
					mousebutton[button]=false;
					keymap[255+button]=false;
				}
			}
		}
		if (released) {
			mousebutton[button]=false;
			keymap[255+button]=false;
		}
	}

	public void mouseClicked(MouseEvent e) {
		// part of the "official" method of handling keyboard focus
		// some people think it's a bug.
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4362074
		if (!has_focus) canvas.requestFocus();
		updateMouse(e,false,false,true); 
	}
	public void mouseEntered(MouseEvent e) {
		updateMouse(e,false,false,true); 
	}
	public void mouseExited(MouseEvent e) {
		updateMouse(e,false,false,false); 
	}
	public void mousePressed(MouseEvent e) {
		updateMouse(e,true,false,true); 
	}
	public void mouseReleased(MouseEvent e) {
		updateMouse(e,false,true,true); 
	}
	public void mouseDragged(MouseEvent e) {
		updateMouse(e,false,false,true); 
	}
	public void mouseMoved(MouseEvent e) {
		updateMouse(e,false,false,true); 
	}
	public void focusGained(FocusEvent e) {
		has_focus=true;
	}
	public void focusLost(FocusEvent e) {
		has_focus=false;
	}

	/* Standard Wimp event handlers */
	public void keyPressed(KeyEvent e) {
		char keychar = e.getKeyChar();
		int keycode = e.getKeyCode();
		if (keycode>=0 && keycode < 256) {
			keymap[keycode]=true;
			lastkey=keycode;
			lastkeychar=keychar;
			if (wakeup_key==-1 || wakeup_key==keycode) {
				if (!eng.isRunning()) {
					eng.start();
					// key is cleared when it is used as wakeup key
					keymap[keycode]=false;
				}
			}
		}
		/* shift escape = exit */
		if (e.isShiftDown () 
		&& e.getKeyCode () == KeyEvent.VK_ESCAPE 
		&& !eng.isApplet()) {
			System.exit(0);
		}
		//System.out.println(e+" keychar"+e.getKeyChar());
	}

	/* handle keys, shift-escape patch by Jeff Friesen */
	public void keyReleased (KeyEvent e) {
		char keychar = e.getKeyChar ();
		int keycode = e.getKeyCode ();
		if (keycode >= 0 && keycode < 256) {
			keymap [keycode] = false;
		}
	}
	public void keyTyped (KeyEvent e) { }


	/* WindowListener handlers */

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {System.out.println("Closed");}
	public void windowClosing(WindowEvent e) {
		System.out.println("Window closed; exiting.");
		closeWindow();
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}


	/* components */

	JGEngineInterface eng;
	EngineLogic el;
	Component canvas;

	/* window */

	Window my_win;
	Frame my_frame;
	/** indicates if application window should have decoration */
	boolean win_decoration=true;
	/** indicates if an application window should be created, N/A for applets */
	boolean create_frame=true;

	/* mouse */

	boolean has_focus=false;
	Point mousepos = new Point(0,0);
	boolean [] mousebutton = new boolean[] {false,false,false,false};
	boolean mouseinside=false;

	/* keyboard */

	/** The codes 256-258 are the mouse buttons */
	boolean [] keymap = new boolean [256+3];
	int lastkey=0;
	char lastkeychar=0;
	int wakeup_key=0;

	public void clearKeymap() {
		for (int i=0; i<256+3; i++) keymap[i]=false;
	}

	public void wakeUpOnKey(int key) { wakeup_key=key; }

	/* input */

	// get methods unnecessary, variables accessed directly from JGEngine

	public JGPoint getMousePos() { return new JGPoint(mousepos.x,mousepos.y); }
	public int getMouseX() { return mousepos.x; }
	public int getMouseY() { return mousepos.y; }

	public boolean getMouseButton(int nr) { return mousebutton[nr]; }
	public void clearMouseButton(int nr) { mousebutton[nr]=false; }
	public void setMouseButton(int nr) { mousebutton[nr]=true; }
	public boolean getMouseInside() { return mouseinside; }

	public boolean getKey(int key) { return keymap[key]; }
	public void clearKey(int key) { keymap[key]=false; }
	public void setKey(int key) { keymap[key]=true; }

	public int getLastKey() { return lastkey; }
	public char getLastKeyChar() { return lastkeychar; }


	public void clearLastKey() {
		lastkey=0;
		lastkeychar=0;
	}

	public static String getKeyDescStatic(int key) {
		if (key==32) return "space";
		if (key==0) return "(none)";
		if (key==JGEngineInterface.KeyEnter) return "enter";
		if (key==JGEngineInterface.KeyEsc) return "escape";
		if (key==JGEngineInterface.KeyUp) return "cursor up";
		if (key==JGEngineInterface.KeyDown) return "cursor down";
		if (key==JGEngineInterface.KeyLeft) return "cursor left";
		if (key==JGEngineInterface.KeyRight) return "cursor right";
		if (key==JGEngineInterface.KeyShift) return "shift";
		if (key==JGEngineInterface.KeyAlt) return "alt";
		if (key==JGEngineInterface.KeyCtrl) return "control";
		if (key==JGEngineInterface.KeyMouse1) return "left mouse button";
		if (key==JGEngineInterface.KeyMouse2) return "middle mouse button";
		if (key==JGEngineInterface.KeyMouse3) return "right mouse button";
		if (key==27) return "escape";
		if (key >= 33 && key <= 95)
			return new String(new char[] {(char)key});
		return "keycode "+key;
	}

	public static int getKeyCodeStatic(String keydesc) {
		// tab, enter, backspace, insert, delete, home, end, pageup, pagedown
		// escape
		keydesc = keydesc.toLowerCase().trim();
		if (keydesc.equals("space")) {
			return 32;
		} else if (keydesc.equals("escape")) {
			return JGEngineInterface.KeyEsc;
		} else if (keydesc.equals("(none)")) {
			return 0;
		} else if (keydesc.equals("enter")) {
			return JGEngineInterface.KeyEnter;
		} else if (keydesc.equals("cursor up")) {
			return JGEngineInterface.KeyUp;
		} else if (keydesc.equals("cursor down")) {
			return JGEngineInterface.KeyDown;
		} else if (keydesc.equals("cursor left")) {
			return JGEngineInterface.KeyLeft;
		} else if (keydesc.equals("cursor right")) {
			return JGEngineInterface.KeyRight;
		} else if (keydesc.equals("shift")) {
			return JGEngineInterface.KeyShift;
		} else if (keydesc.equals("alt")) {
			return JGEngineInterface.KeyAlt;
		} else if (keydesc.equals("control")) {
			return JGEngineInterface.KeyCtrl;
		} else if (keydesc.equals("left mouse button")) {
			return JGEngineInterface.KeyMouse1;
		} else if (keydesc.equals("middle mouse button")) {
			return JGEngineInterface.KeyMouse2;
		} else if (keydesc.equals("right mouse button")) {
			return JGEngineInterface.KeyMouse3;
		} else if (keydesc.startsWith("keycode")) {
			return Integer.parseInt(keydesc.substring(7));
		} else if (keydesc.length() == 1) {
			return keydesc.charAt(0);
		}
		return 0;
	}

	// not input device

	public String getConfigPath(String filename) {
		if (eng.isApplet()) return null;
		File jgamedir;
		try {
			jgamedir = new File(System.getProperty("user.home"), ".jgame");
		} catch (Exception e) {
			// probably AccessControlException of unsigned webstart
			return null;
		}
		if (!jgamedir.exists()) {
			// try to create ".jgame"
			if (!jgamedir.mkdir()) {
				// fail
				return null;
			}
		}
		if (!jgamedir.isDirectory()) return null;
		File file = new File(jgamedir,filename);
		// try to create file if it didn't exist
		try {
			file.createNewFile();
		} catch (IOException e) {
			return null;
		}
		if (!file.canRead()) return null;
		if (!file.canWrite()) return null;
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}


	/** Note: this assumes that primitive type wrappers:
	* Integer, Char, Boolean, Double, Float are always primitive types */
	@SuppressWarnings("unchecked")
	static Method getMethod(Class cls,String name,Object [] args) {
		Class [] args_cls = new Class[args.length];
		for (int i=0; i<args.length; i++) {
			if (args[i] instanceof Boolean) {
				args_cls[i] = Boolean.TYPE;
			} else if (args[i] instanceof Character) {
				args_cls[i] = Character.TYPE;
			} else if (args[i] instanceof Integer) {
				args_cls[i] = Integer.TYPE;
			} else if (args[i] instanceof Double) {
				args_cls[i] = Double.TYPE;
			} else if (args[i] instanceof Float) {
				args_cls[i] = Float.TYPE;
			} else if (args[i] instanceof GraphicsConfiguration) {
				// hack to make subclasses of GraphicsConfiguration work
				args_cls[i] = GraphicsConfiguration.class;
			} else {
				args_cls[i] = args[i].getClass();
			}
		}
		try {
			return cls.getMethod(name, args_cls);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/** Try to execute given method on given object.  Handles invocation
	 * target exceptions.
	 * @return true = method exists and has been invoked */
	boolean tryMethod(Object o,String name,Object [] args) {
		try {
			Method met=getMethod(o.getClass(),name,args);
			if (met==null) return false;
			met.invoke(o,args);
		} catch (InvocationTargetException ex) {
			Throwable ex_t = ex.getTargetException();
			if (ex_t instanceof JGameError) {
				eng.exitEngine(eng.dbgExceptionToString(ex_t));
			} else {
				eng.dbgShowException("MAIN",ex_t);
			}
			return false;
		} catch (IllegalAccessException ex) {
			System.err.println("Unexpected exception:");
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	static boolean existsMethod(Class cls,String name,Object [] args) {
		return getMethod(cls,name,args)!=null;
	}

	@SuppressWarnings("unchecked")
	static boolean existsMethod(Class cls,String name,Class [] args) {
		try {
			cls.getMethod(name, args);
			return true;
		} catch (NoSuchMethodException ex) {
			return false;
		}
	}

	/** create window to simulate applet window. */
	void createWindow(Component appwin,boolean add_decoration) {
		/* create window to `emulate' an applet's frame */
		if (existsMethod(Frame.class,"setUndecorated",new Class[] {
		Boolean.TYPE } )) {
			/* this is the jdk1.4+ way to do it */
			my_win = new Frame();
			tryMethod(my_win,"setUndecorated",new Object[] {
				new Boolean(!add_decoration) } );
		} else {
			/* jdk1.2: adding a window to a frame like this results in a
			* window without decoration. */
			if (!add_decoration) {
				my_frame = new Frame();
				my_win = new Window(my_frame);
				/* in jdk1.4, we need to call the following two methods to
				* ensure we can get the focus. However, jdk1.2 doesn't have the
				* setFocusableWindowState method however. */
				//my_win.setFocusableWindowState(true);
				tryMethod(my_win,"setFocusableWindowState",new Object[] {
					new Boolean(true) } );
				my_frame.setVisible(true);
				//tryMethod(my_frame,"setVisible",new Object[] {
				//	new Boolean(true) };
			} else {
				my_win = new Frame();
			}
		}
		//my_win.setResizable(false);
		// ensure no margins around canvas
		my_win.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		setWindowSize(add_decoration);
		my_win.add(appwin);
		my_win.addWindowListener(this);
	}

	/** Set or reset window size according to known insets. */
	void setWindowSize(boolean add_decoration) {
		if (!add_decoration) {
			my_win.setSize(el.winwidth, el.winheight);
		} else {
			// setting the size of the canvas or applet has no effect
			// we need to add the height of the title bar to the height
			// We use the insets now. Originally, we used:
			// 24 is the empirically determined height in WinXP
			// 48 enables us to have the whole window with title bar on-screen
			// 8 is the empirically determined width in win and linux
			Insets insets = my_win.getInsets();
			my_win.setSize(el.winwidth+insets.left+insets.right,
				el.winheight+insets.top+insets.bottom);
		}

	}
	void closeWindow() {
		my_win.setVisible(false);
		System.exit(0);
	}


	/*===== audio =====*/


	/** channelname -} clipid -} AudioClip.  Clipid and AudioClip are not
	* defined until played at least once.  */
	@SuppressWarnings("unchecked")
	Hashtable channels = new Hashtable();

	/** channelname -} clipid.  Sample has been played last as non-loop. */
	@SuppressWarnings("unchecked")
	Hashtable lastplayed = new Hashtable();
	/** channelname -} clipid.  Sample is playing as loop. */
	@SuppressWarnings("unchecked")
	Hashtable islooping = new Hashtable();

	/** clipd -} "yes". Sample has already been triggered on an unnamed
	* channel during this frame. */
	@SuppressWarnings("unchecked")
	Hashtable clipstriggered = new Hashtable();

	int unnamedchnr = 0;
	int nr_unnamedch = 12;

	boolean audioenabled=true;

	/** signal to audio subsystem that new frame has started. */
	@SuppressWarnings("unchecked")
	void audioNewFrame() {
		clipstriggered = new Hashtable();
	}

	/** Enable audio, restart any audio loops. */
	@SuppressWarnings("unchecked")
	public void enableAudio() {
		if (audioenabled==true) return;
		audioenabled=true;
		for (Enumeration e=channels.keys(); e.hasMoreElements(); ) {
			String channel = (String)e.nextElement();
			String lastclipid=(String)islooping.get(channel);
			if (lastclipid==null) continue;
			Hashtable chan = (Hashtable)channels.get(channel);
			AudioClip clip = (AudioClip)chan.get(lastclipid);
			if (clip!=null) clip.loop(); 
		}
	}

	/** Disable audio, stop all currently playing audio.  Audio commands will
	* be ignored, except that audio loops (music, ambient sounds) are
	* remembered and will be restarted once audio is enabled again. */
	@SuppressWarnings("unchecked")
	public void disableAudio() {
		if (audioenabled==false) return;
		audioenabled=false;
		for (Enumeration e=channels.keys(); e.hasMoreElements(); ) {
			String channel = (String)e.nextElement();
			String lastclipid=(String)lastplayed.get(channel);
			if (lastclipid==null) continue;
			Hashtable chan = (Hashtable)channels.get(channel);
			AudioClip clip = (AudioClip)chan.get(lastclipid);
			if (clip!=null) clip.stop(); 
		}
	}

	private AudioClip loadAudioClip(Applet applet,String clipid) {
		URL clipres = getClass().getResource(
				(String) el.audioclips.get(clipid) );
		AudioClip clip;
		if (eng.isApplet()) {
			clip=applet.getAudioClip(clipres);
		} else {
			clip=Applet.newAudioClip(clipres);
		}
		return clip;
	}

	public String lastPlayedAudio(String channel) {
		return (String)lastplayed.get(channel);
	}

	@SuppressWarnings("unchecked")
	public void playAudio(Applet applet,String clipid) {
		if (clipstriggered.containsKey(clipid)) return;
		clipstriggered.put(clipid,"yes");
		playAudio(applet,"_unnamed"+unnamedchnr,clipid,false);
		unnamedchnr = (unnamedchnr+1)%nr_unnamedch;
	}

	@SuppressWarnings("unchecked")
	public void playAudio(Applet applet,String channel,String clipid,boolean loop) {
		AudioClip clip = null;
		Hashtable chan = (Hashtable) channels.get(channel);
		String clipplaying = (String)lastplayed.get(channel);
		if (chan!=null) {
			clip = (AudioClip) chan.get(clipid);
		} else {
			chan = new Hashtable();
			channels.put(channel,chan);
		}
		if (clip==null) {
			clip = loadAudioClip(applet,clipid);
			chan.put(clipid,clip);
		}
		boolean restart=true;
		if (clipplaying!=null && !clipplaying.equals(clipid)) {
			AudioClip prevclip = (AudioClip) chan.get(clipplaying);
			if (audioenabled) prevclip.stop();
		} else {
			// previous clip is same as this one
			String looping = (String)islooping.get(channel);
			if (loop && looping!=null && looping.equals(clipid)) {
				// both are looping, don't do anything
				restart=false;
			} else {
				// other is not looping, restart
				restart=true;
			}
		}
		if (loop) {
			if (restart) {
				if (audioenabled) clip.loop();
				islooping.put(channel,clipid);
			}
		} else {
			if (audioenabled) clip.play();
			islooping.remove(channel);
		}
		lastplayed.put(channel,clipid);
	}

	@SuppressWarnings("unchecked")
	public void stopAudio(String channel) {
		String lastclipid = (String) lastplayed.get(channel);
		if (lastclipid==null) return;
		Hashtable chan = (Hashtable)channels.get(channel);
		AudioClip clip = (AudioClip)chan.get(lastclipid);
		if (clip!=null) if (audioenabled) clip.stop(); 
		lastplayed.remove(channel);
		islooping.remove(channel);
	}

	@SuppressWarnings("unchecked")
	public void stopAudio() {
		for (Enumeration e=channels.keys(); e.hasMoreElements(); ) {
			stopAudio((String)e.nextElement());
		}
	}

	String localstorefile;

	@SuppressWarnings("unchecked")
	Hashtable localstore = new Hashtable();

	@SuppressWarnings("unchecked")
	void storeInit() {
		localstorefile = getConfigPath(eng.getClass().getName()+".store");
		if (localstorefile==null) return;
		try {
			BufferedReader reader;
			try {
				File file = new File(localstorefile);
				// do nothing if file does not yet exist
				if (!file.exists()) return;
				reader = new BufferedReader(new FileReader(file));
			} catch (java.security.AccessControlException e) {
				//we're not allowed to look at the file, try it as url
				URL url = new URL(localstorefile);
				reader = new BufferedReader(new InputStreamReader(
						url.openStream()));
			}
			//StringBuffer text = new StringBuffer();
			String line;
			while (true) {
				line = reader.readLine();
				if (line==null) break;
				String name = line.substring(0,line.indexOf("\t"));
				String rest1 = line.substring(line.indexOf("\t")+1);
				String type = rest1.substring(0,rest1.indexOf("\t"));
				String value = rest1.substring(rest1.indexOf("\t")+1);
				if (type.equals("int")) {
					localstore.put(name,new Integer(Integer.parseInt(value)));
				} else if (type.equals("double")) {
					localstore.put(name,new Double(Double.parseDouble(value)));
				} else if (type.equals("String")) {
					localstore.put(name,value);
				} else {
					throw new JGameError("Unknown type "+type);
				}
			}
			reader.close();
		} catch (IOException e) {
			throw new JGameError("Error reading file '"+localstorefile+"'.",false);
		}
	}

	@SuppressWarnings("unchecked")
	private void storeWrite() { try {
		if (localstorefile==null) return;
		PrintWriter writer=new PrintWriter(
			new FileWriter(new File(localstorefile)) );
		for (Enumeration e=localstore.keys(); e.hasMoreElements(); ) {
			String k = (String)e.nextElement();
			Object o = localstore.get(k);
			if (o instanceof Integer) {
				writer.println(k+"\tint\t"+o);
			} else if (o instanceof Double) {
				writer.println(k+"\tdouble\t"+o);
			} else if (o instanceof String) {
				writer.println(k+"\tString\t"+o);
			}
		}
		writer.close();
	} catch (IOException e) {
		throw new JGameError("Error writing file '"+localstorefile+"'.",false);
	} }

	@SuppressWarnings("unchecked")
	public void storeWriteInt(String id,int value) {
		localstore.put(id,new Integer(value));
		storeWrite();
	}

	@SuppressWarnings("unchecked")
	public void storeWriteDouble(String id,double value) {
		localstore.put(id,new Double(value));
		storeWrite();
	}

	// XXX strings are not escaped, this means \n is not allowed.
	// fix: escape \n as '\n' and '\' as '\\'

	@SuppressWarnings("unchecked")
	public void storeWriteString(String id,String value) {
		localstore.put(id,value);
		storeWrite();
	}

	public void storeRemove(String id) {
		localstore.remove(id);
		storeWrite();
	}

	public boolean storeExists(String id) {
		return localstore.containsKey(id);
	}

	public int storeReadInt(String id,int undef) {
		if (!localstore.containsKey(id)) return undef;
		return ((Integer)localstore.get(id)).intValue();
	}

	public double storeReadDouble(String id,double undef) {
		if (!localstore.containsKey(id)) return undef;
		return ((Double)localstore.get(id)).doubleValue();
	}

	public String storeReadString(String id,String undef) {
		if (!localstore.containsKey(id)) return undef;
		return (String)localstore.get(id);
	}

	/*====== options ======*/

	public void optsAddTitle(String title) {}

	public void optsAddNumber(String varname,String title,String desc,
	int decimals, double lower,double upper,double step, double initial) {}

	public void optsAddBoolean(String varname,String title,String desc,
	boolean initial) {}

	public void optsAddEnum(String varname,String title,String desc,
	String [] values, int initial) {}

	public void optsAddKey(String varname,String title,String desc,int initial){
	}

	public void optsAddString(String varname,String title,String desc,
	int maxlen, boolean isPassword, String initial) {}

	public void optsClear() {}


}

