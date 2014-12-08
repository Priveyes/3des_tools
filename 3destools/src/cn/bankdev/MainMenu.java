package cn.bankdev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;

import com.ibm.icu.util.Calendar;

public class MainMenu
{
  protected Shell shldes;
  private Text key;
  private Text encry;
  private Text outpath;
  private Combo c_type;

  public static void main(String[] args)
  {
    try
    {
      MainMenu window = new MainMenu();
      window.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void open()
    throws IOException
  {
    Display display = Display.getDefault();
    createContents();
    this.shldes.open();
    this.shldes.layout();
    while (!this.shldes.isDisposed())
      if (!display.readAndDispatch())
        display.sleep();
  }

  protected void createContents()
    throws IOException
  {
    this.shldes = new Shell();
    this.shldes.setSize(465, 318);
    this.shldes.setText("3DES加密软件");

    Label label = new Label(this.shldes, 0);
    label.setBounds(31, 41, 90, 22);
    label.setText("请输入24位密钥:");

    this.key = new Text(this.shldes, 2048);
    this.key.setBounds(126, 41, 289, 25);
    this.key.setTextLimit(24);
    
    c_type = new Combo(shldes, SWT.NONE);
    c_type.setItems(new String[] {"10", "30", "50", "100", "300"});
    c_type.setBounds(126, 185, 88, 25);

    
    Label label_2 = new Label(shldes, SWT.NONE);
    label_2.setText("\u8BF7\u9009\u62E9\u9762\u503C\u5927\u5C0F:");
    label_2.setBounds(31, 185, 90, 18);
    

    File file1 = new File("c:/key.txt");
    if (!file1.exists())
    {
      file1.createNewFile();
      BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1, false));
      bw1.write("myth1234leoyul$5galo62sf");
      bw1.flush();
      bw1.close();
    }
    else {
      BufferedReader reader1 = new BufferedReader(new FileReader(file1));
      if (reader1 != null)
      {
        String tstring = null;
        tstring = reader1.readLine();
        this.key.setText(tstring);
        reader1.close();
      }
    }
    Label lblQ = new Label(this.shldes, 0);
    lblQ.setText("待加密文件:");
    lblQ.setBounds(31, 87, 90, 18);

    this.encry = new Text(this.shldes, 2048);
    this.encry.setBounds(126, 83, 289, 25);

    Button btnNewButton = new Button(this.shldes, 0);
    btnNewButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        FileDialog dialog = new FileDialog(MainMenu.this.shldes, 4096);
        dialog.setText("请选择待加密文件");
        //String filepath = dialog.open();
        dialog.open();
        dialog.setFilterExtensions(new String[] { "*.*" });
        String	filepath = null;
        filepath = dialog.getFilterPath()+"\\"+dialog.getFileName();
        if (filepath!=null&&!filepath.equals("\\"))
        {
          MainMenu.this.encry.setText(filepath);
        }
      }
    });
    btnNewButton.setBounds(343, 111, 72, 22);
    btnNewButton.setText("浏览文件");

    Button button = new Button(this.shldes, 0);
    button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (MainMenu.this.key.getText().length() != 24)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("错误提示!");
          mb.setMessage("请输入24位密钥，必须与对方保持一致");
          mb.open();
          return;
        }
        if (MainMenu.this.encry.getText().length() == 0)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("错误提示!");
          mb.setMessage("请选择待加密文件！");
          mb.open();
          return;
        }
        if (MainMenu.this.outpath.getText().length() == 0)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("错误提示!");
          mb.setMessage("请选择输出文件路径！");
          mb.open();
          return;
        }
      
        if (MainMenu.this.c_type.getText().length()==0)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("错误提示!");
          mb.setMessage("请选择面值类型！");
          mb.open();
          return;
        }
        
        
        File file = new File(MainMenu.this.encry.getText());
        BufferedReader reader = null;
        try
        {
          System.out.println("以行开始读取文件");
          reader = new BufferedReader(new FileReader(file));
          String tempString = null;
          int line = 0;

          File f = new File(MainMenu.this.outpath.getText() + "/" + "encry.avl");
          BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
          bw.write("");
          int j=0;//行数
          String	enddate=null;
          while ((tempString = reader.readLine()) != null)
          {
            System.out.println("line" + line + ":" + tempString);
            
            if(j<15)
            {
            	if(j==7)
            	{
            		String[]	tmpstring=tempString.split(":");
            		enddate = tmpstring[1];
            	}
            	j++;
            	continue;
            }
            String[] strarray = tempString.split(" ");
            Desfunc df = new Desfunc();
            byte[] keyBytes = null;
            try {
              keyBytes = Desfunc.build3DesKey(MainMenu.this.key.getText());
            }
            catch (UnsupportedEncodingException e2)
            {
              e2.printStackTrace();
            }
            String tmpencrystr = Desfunc.byte2Hex(Desfunc.encryptMode(keyBytes, strarray[1].getBytes()));
            strarray[1] = tmpencrystr;
            System.out.println(Arrays.toString(strarray));
//            String tpstr = strarray[0] + " " + strarray[1] + " " + strarray[2] + " " + strarray[3];
            String tpstr = strarray[0] + " " + strarray[1] + " " + enddate + " " + "SYJ";
            System.out.println("result string is : " + tpstr);
            line++;
            bw.write(tpstr);
            bw.newLine();
          }

          MessageBox mb = new MessageBox(MainMenu.this.shldes, 32);
          mb.setText("成功提示!");
          mb.setMessage("加密成功，本次加密条数: " + line);
          mb.open();
          bw.close();
          reader.close();
          /** 修改文件名称 **/
          /** 文件命名规则
          DZCARD_生产日期(YYYYMMDDSSSS)_卡类型(面值)_卡商_数量.avl
           **/
          String	rootpath = f.getParent();
          Calendar	cal = Calendar.getInstance();
          int	year = cal.get(Calendar.YEAR);
          int	mon = cal.get(Calendar.MONTH)+1;
          int	day = cal.get(Calendar.DAY_OF_MONTH);
          int	hour = cal.get(Calendar.HOUR);
          int	min = cal.get(Calendar.MINUTE);
          int	sec = cal.get(Calendar.SECOND);
          String	syear= Integer.toString(year);
          String    smon;
          String	sday;
          String	shour;
          String	smin;
          String	ssec;
          if(mon<10)
          {
              	smon="0"+Integer.toString(mon);
          }else
          {
        	 	smon=Integer.toString(mon);
          }
          if(day<10)
          {
        	  	sday = "0"+Integer.toString(day);
          }else
          {
        	  	sday = "0"+Integer.toString(day);
          }
          if(hour<10)
          {
        	  	shour="0"+Integer.toString(hour);
          }else
          {
        	  	shour=Integer.toString(hour);
          }
          if(min<10)
          {
        	 	smin="0"+Integer.toString(min);
          }else
          {
        	  	smin=Integer.toString(min);
          }
          if(sec<10)
          {
        	  	ssec="0"+Integer.toString(sec);
          }else
          {
        	  	ssec=Integer.toString(sec);
          }
          String	da = syear+smon+sday+shour+smin+ssec;
          File rnamefile = new File(rootpath+File.separator+"DZCARD"+"_"+da+"_"+c_type.getText()+"_"+"SYJ"+"_"+line+"."+"avl");
          if(f.renameTo(rnamefile))
          {
        	  System.out.println("修改文件成功");
          }else
          {
        	  System.out.println("修改文件失败");
          }
        }
        catch (IOException e1) {
          e1.printStackTrace();

          if (reader != null)
          {
            try
            {
              reader.close();
            }
            catch (IOException e11) {
              e11.printStackTrace();
            }
          }
        }
        finally
        {
          if (reader != null)
          {
            try
            {
              reader.close();
            }
            catch (IOException e1) {
              e1.printStackTrace();
            }
          }
        }
      }
    });
    button.setText("开始加密");
    button.setBounds(343, 215, 72, 22);

    Label lblNewLabel = new Label(this.shldes, 0);
    lblNewLabel.setBounds(252, 243, 187, 26);
    lblNewLabel.setText("www.bankdev.cn:无业游民工作室");

    Label label_1 = new Label(this.shldes, 0);
    label_1.setText("输出文件路径:");
    label_1.setBounds(31, 139, 90, 18);

    this.outpath = new Text(this.shldes, 2048);
    this.outpath.setBounds(126, 139, 289, 25);

    Button button_1 = new Button(this.shldes, 0);
    button_1.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        DirectoryDialog dialog = new DirectoryDialog(MainMenu.this.shldes, 4096);
        dialog.setText("请选择输出文件路径");
        dialog.open();
        String filepath = null;
        filepath = dialog.getFilterPath();
        if (filepath != null&&!filepath.equals("\\"))
        {
          MainMenu.this.outpath.setText(filepath);
        }
      }
    });
    button_1.setText("选择目录");
    button_1.setBounds(343, 170, 72, 22);
  }
}