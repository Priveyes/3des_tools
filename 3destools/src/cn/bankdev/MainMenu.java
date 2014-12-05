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

public class MainMenu
{
  protected Shell shldes;
  private Text key;
  private Text encry;
  private Text outpath;

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
    this.shldes.setText("3DES�������");

    Label label = new Label(this.shldes, 0);
    label.setBounds(31, 41, 90, 22);
    label.setText("������24λ��Կ:");

    this.key = new Text(this.shldes, 2048);
    this.key.setBounds(126, 41, 289, 25);
    this.key.setTextLimit(24);

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
    lblQ.setText("�������ļ�:");
    lblQ.setBounds(31, 87, 90, 18);

    this.encry = new Text(this.shldes, 2048);
    this.encry.setBounds(126, 83, 289, 25);

    Button btnNewButton = new Button(this.shldes, 0);
    btnNewButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        FileDialog dialog = new FileDialog(MainMenu.this.shldes, 4096);
        dialog.setText("Source Folder Selection");
        dialog.setFilterExtensions(new String[] { "*.*" });
        String filepath = dialog.open();
        if (dialog != null)
        {
          MainMenu.this.encry.setText(filepath);
        }
      }
    });
    btnNewButton.setBounds(343, 111, 72, 22);
    btnNewButton.setText("����ļ�");

    Button button = new Button(this.shldes, 0);
    button.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        if (MainMenu.this.key.getText().length() != 24)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("������ʾ!");
          mb.setMessage("������24λ��Կ��������Է�����һ��");
          mb.open();
          return;
        }
        if (MainMenu.this.encry.getText().length() == 0)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("������ʾ!");
          mb.setMessage("��ѡ��������ļ���");
          mb.open();
          return;
        }
        if (MainMenu.this.outpath.getText().length() == 0)
        {
          MessageBox mb = new MessageBox(MainMenu.this.shldes, 1);
          mb.setText("������ʾ!");
          mb.setMessage("��ѡ������ļ�·����");
          mb.open();
          return;
        }

        File file = new File(MainMenu.this.encry.getText());
        BufferedReader reader = null;
        try
        {
          System.out.println("���п�ʼ��ȡ�ļ�");
          reader = new BufferedReader(new FileReader(file));
          String tempString = null;
          int line = 0;
          File f = new File(MainMenu.this.outpath.getText() + "/" + "encry.avl");
          BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
          bw.write("");
          int j=0;//����
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
          mb.setText("�ɹ���ʾ!");
          mb.setMessage("���ܳɹ������μ�������: " + line);
          mb.open();
          bw.close();
          reader.close();
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
    button.setText("��ʼ����");
    button.setBounds(343, 215, 72, 22);

    Label lblNewLabel = new Label(this.shldes, 0);
    lblNewLabel.setBounds(252, 243, 187, 26);
    lblNewLabel.setText("www.bankdev.cn:��ҵ��������");

    Label label_1 = new Label(this.shldes, 0);
    label_1.setText("����ļ�·��:");
    label_1.setBounds(31, 139, 90, 18);

    this.outpath = new Text(this.shldes, 2048);
    this.outpath.setBounds(126, 139, 289, 25);

    Button button_1 = new Button(this.shldes, 0);
    button_1.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e) {
        DirectoryDialog dialog = new DirectoryDialog(MainMenu.this.shldes, 4096);
        dialog.setText("Source Folder Selection");
        String filepath = dialog.open();
        if (dialog != null)
        {
          MainMenu.this.outpath.setText(filepath);
        }
      }
    });
    button_1.setText("ѡ��Ŀ¼");
    button_1.setBounds(343, 170, 72, 22);
  }
}