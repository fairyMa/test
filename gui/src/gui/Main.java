package gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;






class MyShape{
	Shape s;
	Color c;
	int y=0;
	int x=0;
	Map<Integer,String>map=new HashMap();
	Map<Integer,Color>color=new HashMap();
}
class MyString{
	Font f;//字体
	String s;//画的线
	Color c;
	int x;
	int y;
}
class DrawPanel extends JPanel{
	ArrayList<MyShape>shapelist=new ArrayList();//形状，颜色
	ArrayList<MyString>stringlist=new ArrayList();
	ArrayList<MyShape>shapelist1=new ArrayList();
	MyShape tempshape;
	Color currColor;
	BtnHandler bt;
	boolean same=false;
	Shape s;
	
	int x1,x2,y1,y2;
	
	DrawPanel(BtnHandler bt){
		this.bt=bt;
		DrawMouseL ml=new DrawMouseL();
		this.addMouseListener(ml);
		DrawMouseML mml=new DrawMouseML();
		this.addMouseMotionListener(mml);
	}
	@Override
	public void paintComponent(Graphics g) {
		if(bt.x==5)  {
			g.clearRect(0,0,getSize().width,getSize().height);
			shapelist=new ArrayList();
			tempshape=null;
			stringlist=new ArrayList();
			shapelist1=new ArrayList();
		}
		else {
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D) g;
		for(MyShape ms:shapelist) {
			g2d.setColor(ms.c);
			g2d.draw(ms.s);
			if(bt.y==1) {
				ms.y=ms.y+1;
				ms.map.put(ms.y,"实心");
				shapelist1.add(ms);
			}
			else {
				ms.y=ms.y+1;
				ms.map.put(ms.y,"空心");
			}
		}
		for(MyShape ms:shapelist1) {
			ms.x=ms.x+1;
			ms.color.put(ms.x,bt.c);
			//System.out.println(ms.y);
			if(ms.map.get(1)=="实心") {
			g2d.setColor(ms.color.get(1));
			g2d.fill(ms.s);
			}
		}
		if(tempshape!=null) {
			g2d.setColor(tempshape.c);
			g2d.draw(tempshape.s);
		}
		for(MyString ms:stringlist) {
			g2d.setColor(ms.c);
			g2d.setFont(ms.f);
			if(ms.s!=null) g2d.drawString(ms.s,ms.x,ms.y);
		}
		
		if(bt.path!=null) {
			Image image =null;
			try {
				image = ImageIO.read(new File(bt.path));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	g.drawImage(image, 0, 0, this);  
		}
		repaint();
		}
		
	}
	
	class DrawMouseML extends MouseMotionAdapter{
		
		public void mouseDragged(MouseEvent e) {
			if(bt.x==0) {
			if(tempshape!=null) {
				Line2D l=(Line2D)(tempshape.s);
				l.setLine(l.getX1(), l.getY1(), e.getX(), e.getY());
			shapelist.add(tempshape);
		    }
		tempshape=new MyShape();
		
			Line2D l=new Line2D.Double();
			l.setLine(e.getX(), e.getY(), e.getX(), e.getY());
			tempshape.c=currColor;
			tempshape.s=l;
			repaint();
		}
		}
	}
class DrawMouseL extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e){
		if(tempshape!=null) {
			shapelist.add(tempshape);
		    }
		tempshape=new MyShape();
		
		if(bt.x==0||bt.x==1) {
			Line2D l=new Line2D.Double();
			l.setLine(e.getX(), e.getY(), e.getX(), e.getY());
			tempshape.c=currColor;
			tempshape.s=l;
			}
		else if(bt.x==3) {
			   Rectangle2D r=new Rectangle2D.Double();
			r.setRect(e.getX(), e.getY(), 0, 0);
			tempshape.c=currColor;
			tempshape.s=r;
			s=r;
		   }
		else if(bt.x==4) {
			   Ellipse2D el=new Ellipse2D.Double();
			el.setFrame(e.getX(), e.getY(), 0, 0);
			tempshape.c=currColor;
			tempshape.s=el;
		   }
		else if(bt.x==2) {
		   QuadCurve2D.Double c = new QuadCurve2D.Double(e.getX(), e.getY(),e.getX(), e.getY(),e.getX(), e.getY());
					tempshape.c=currColor;
					tempshape.s=c;
		   }

			x1=e.getX();
			y1=e.getY();
			
		}
		@Override
		public void mouseReleased(MouseEvent e){
		if(bt.x==1) { 
			Line2D l=(Line2D)(tempshape.s);
			l.setLine(l.getX1(), l.getY1(), e.getX(), e.getY());
		   }
		else if(bt.x==3) {				
					Rectangle2D r=(Rectangle2D)(tempshape.s);
					r.setRect(r.getX(), r.getY(), e.getX()-r.getX(), e.getY()-r.getY());	
					s=r;
			}
		else if(bt.x==4) {
			  Ellipse2D el=(Ellipse2D)(tempshape.s);
			el.setFrame(el.getCenterX(), el.getCenterY(), e.getX()-el.getCenterX(), e.getY()-el.getCenterY());
		  }
		else if(bt.x==2) {
			  QuadCurve2D.Double c=(QuadCurve2D.Double)(tempshape.s);
			c.setCurve(c.getX1(), c.getY1(), (c.getX1()+e.getX())/2, c.getX1()*e.getX()/10000, e.getX(),e.getY());
		  }
			repaint();
			
			x2=e.getX();
			y2=e.getY();
		   }
			//tempshape.s=l;
		
			
		@Override
		public void mouseClicked(MouseEvent e){ 
			if(e.getButton()==MouseEvent.BUTTON3) {
				String inputValue = JOptionPane.showInputDialog("Please input a value"); 
				Font f=new Font("宋体",10,10);
				MyString ms=new MyString();
				ms.s=inputValue;
				ms.c=currColor;
				ms.x=e.getX();
				ms.y=e.getY();
				stringlist.add(ms);
				repaint();
				}
		}
	}
}


class BtnHandler extends MouseAdapter implements ActionListener{

	Main parent;
	int x=0;
	String path=null;
	int y=0;
	Color c;
	
	public BtnHandler(Main parent) {
		super();
		this.parent = parent;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().getClass()==JButton.class) {
			JButton sur=(JButton)e.getSource();
			if(sur.getText().equals("打开")) {
				JFileChooser jf=new JFileChooser();
				int result = jf.showOpenDialog(parent);
				if (result == JFileChooser.APPROVE_OPTION) {    
					path = jf.getSelectedFile().getAbsolutePath(); 
					 }
			}
			if(sur.getText().equals("保存")) {
			JFileChooser fd = new JFileChooser(); 
			int result=fd.showSaveDialog(null); 
			    File f = fd.getSelectedFile();	
			
			BufferedImage bi = new BufferedImage(parent.dpanel.getSize().width, parent.dpanel.getSize().height, BufferedImage.TYPE_INT_RGB);
			Graphics2D  g2d = bi.createGraphics();
			parent.dpanel.paint(g2d);
try {
	if (result == JFileChooser.APPROVE_OPTION) {   
				
		ImageIO.write(bi, "jpg", new File(f.getAbsolutePath() + ".jpg"));
	}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(sur.getText().equals("选择颜色")) {
				JDialog frame = new JDialog();
			frame.setBounds(
			new Rectangle(
					(int) parent.getBounds().getX() + 50,
			                        (int) parent.getBounds().getY() + 50, 
			                        (int) parent.getBounds().getWidth(), 
			                        (int) parent.getBounds().getHeight()
			                )
			            );
			JPanel jl = new JPanel(new GridLayout(2,2));;
			frame.getContentPane().add(jl);
			JButton one=new JButton("选择背景色");
			JButton two=new JButton("选择前端颜色");
			jl.add(one);
			jl.add(two);
			frame.setSize(50, 300);
			
			frame.setVisible(true);  
			two.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser jf=new JColorChooser();				
							Color c=jf.showDialog(parent, null, null);
							parent.dpanel.currColor=c;
			            }
			        });
			one.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser jf=new JColorChooser();				
							Color c=jf.showDialog(parent, null, null);
							parent.dpanel.setBackground(c);
			            }
			        });   
			}
			if(sur.getText().equals("直线")) {
				x=1;
		    }
			if(sur.getText().equals("曲线")){
				x=2;
		    }
			if(sur.getText().equals("矩形")){
				x=3;
				JDialog frame = new JDialog();//构造一个新的JFrame，作为新窗口。
			frame.setBounds(// 让新窗口与SwingTest窗口示例错开50像素。
			new Rectangle(
					(int) parent.getBounds().getX() + 50,
			                        (int) parent.getBounds().getY() + 50, 
			                        (int) parent.getBounds().getWidth(), 
			                        (int) parent.getBounds().getHeight()
			                )
			            );
			JPanel jl = new JPanel(new GridLayout(2,1));;
			frame.getContentPane().add(jl);
			JButton one=new JButton("选择填充");
			jl.add(one);
			JButton two=new JButton("不选择填充");
			jl.add(two);
			frame.setSize(50, 100);
			
			frame.setVisible(true);  
			one.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
							
							y=1;
							JColorChooser jf=new JColorChooser();				
							c=jf.showDialog(parent, null, null);
			            }
			        }); 
			two.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
						
							y=0;
			            }
			        }); 
		    }
			if(sur.getText().equals("椭圆")){
				x=4;
				JDialog frame = new JDialog();//构造一个新的JFrame，作为新窗口。
			frame.setBounds(// 让新窗口与SwingTest窗口示例错开50像素。
			new Rectangle(
					(int) parent.getBounds().getX() + 50,
			                        (int) parent.getBounds().getY() + 50, 
			                        (int) parent.getBounds().getWidth(), 
			                        (int) parent.getBounds().getHeight()
			                )
			            );
			JPanel jl = new JPanel(new GridLayout(2,1));;
			frame.getContentPane().add(jl);
			JButton one=new JButton("选择填充");
			jl.add(one);
			JButton two=new JButton("不选择填充");
			jl.add(two);
			frame.setSize(50, 150);
			frame.setVisible(true);  
			one.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
							//parent.dpanel.currColor=c;
							y=1;
							JColorChooser jf=new JColorChooser();				
							c=jf.showDialog(parent, null, null);
			            }
			        }); 
			two.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
							//parent.dpanel.currColor=c;
							y=0;
			            }
			        }); 
		    }
			if(sur.getText().equals("画笔")){
				x=0;
		    }
			if(sur.getText().equals("清空")) {
				x=5;
	
			}
	
			}
		}
	
}
public class Main extends JFrame{

	JPanel toolp;
	
	DrawPanel dpanel;
	
	BtnHandler bh;
	
	JButton pen;
	JButton straight_line;
	JButton curved_line;
	JButton rectangle;
	JButton oval;
	JButton btnColorchoose;
	JButton clear;
	JButton save;
	JButton open;
	
	Main(){
		super();
		toolp=new JPanel(new GridLayout(9,1));
		toolp.setBackground(Color.gray);
		
		
		pen=new JButton("画笔");
		straight_line=new JButton("直线");
		curved_line=new JButton("曲线");
		rectangle=new JButton("矩形");
		oval=new JButton("椭圆");
		btnColorchoose=new JButton("选择颜色");
		clear=new JButton("清空");
		save=new JButton("保存");
		open=new JButton("打开");
		
		
		toolp.add(pen);
		toolp.add(straight_line);
		toolp.add(curved_line);
		toolp.add(rectangle);
		toolp.add(oval);
		toolp.add(btnColorchoose);
		toolp.add(clear);
		toolp.add(save);
		toolp.add(open);
		
		this.getContentPane().add("West",toolp);
		toolp.setPreferredSize(new Dimension(150, 50));
		
		bh=new BtnHandler(this);
		
		dpanel=new DrawPanel(bh);
		dpanel.setBackground(Color.white);
		this.getContentPane().add("Center",dpanel);
		
		this.setUndecorated(true); // 去掉窗口的装饰
		this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);// 采用指定的窗口装饰风格
		this.setSize(800,500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addLis();
		this.setVisible(true);		
		
	}
	Main(String title){
		this();
		this.setTitle(title);
		
	}
	
	void addLis(){
		open.addActionListener(bh);
		btnColorchoose.addActionListener(bh);
		straight_line.addActionListener(bh);
		curved_line.addActionListener(bh);
		rectangle.addActionListener(bh);
		oval.addActionListener(bh);
		pen.addActionListener(bh);
		
		clear.addActionListener(bh);
		save.addActionListener(bh);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main ge=new Main("JAVA画图本");
		
		
	}

}


