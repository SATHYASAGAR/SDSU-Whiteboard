package edu.sdsu.cs645.client;

//import edu.sdsu.cs645.shared.FieldVerifier;
import com.google.gwt.core.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;



public class Whiteboard implements EntryPoint {
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";
  private final WhiteboardServiceAsync whiteboardService = GWT.create(WhiteboardService.class);

	private HTML status;
	private RichTextArea board;
	private TextBox tb;
  public void onModuleLoad() {
	  status = new HTML();
	  status.getElement().setId("status_msg");
	  buildLogin();
  }
  
  private void buildLogin(){
	  FlowPanel loginPanel = new FlowPanel();
	  loginPanel.getElement().setId("log_panel");
	  final PasswordTextBox password = new PasswordTextBox();
	  tb = new TextBox();
	  tb.getElement().setId("username_input");
	  password.getElement().setId("password_textbox");
	  loginPanel.add(new HTML("<h1> Welcome to the Whiteboard</h1>"));
	  loginPanel.add(new HTML("<h3> Please enter the password</h3>"));
	  loginPanel.add(new Label("Username"));
	  loginPanel.add(tb);
	  loginPanel.add(new HTML("<br/>"));
	  loginPanel.add(new Label("Password"));
	  loginPanel.add(password);  
	  FlowPanel bPanel = new FlowPanel();
	  bPanel.setStyleName("blog_panel");
	  Button loginButton = new Button("Login");
	  Button clearButton = new Button("Clear");
	  loginButton.setStyleName("log_button"); 
	  clearButton.setStyleName("log_button");
	  bPanel.add(clearButton);
	  bPanel.add(loginButton);
	  clearButton.addClickHandler(new ClickHandler(){
		public void onClick(ClickEvent e){
				status.setText("");
				password.setText("");
				password.setFocus(true);
			}
		});
		loginButton.addClickHandler(new ClickHandler(){
		public void onClick(ClickEvent e){
				validateLogin(password.getText());
				password.setFocus(true);
			}
		});
	  bPanel.add(new HTML("<br/> Code by Esha Vishwakarma, jadrn063"));
	  loginPanel.add(bPanel);
	  loginPanel.add(status);
	  RootPanel.get().add(loginPanel);
	  password.setFocus(true);
  }

	private void validateLogin(String login){
		AsyncCallback callback = new AsyncCallback(){
			public void onSuccess(Object results){
				String answer = (String) results;
				if(answer.equals("OK")){
					status.setText("");
					buildMainPanel();
					}
				else
					status.setText("ERROR, Invalid password");
				}
	
			public void onFailure(Throwable err){
				status.setText("Failed" + err.getMessage());
				err.printStackTrace();				
	        }
    };
		whiteboardService.validateLogin(login,callback);
  }



	private void buildMainPanel(){
		FlowPanel main = new FlowPanel();
		main.add(new HTML("<h3>Online Whiteboard</h3>"));
		main.add(getButtonPanel());
		board = new RichTextArea();
		board.getElement().setId("board_richtext");
		main.add(board);
		main.add(status);
		main.add(new HTML("<h3> Code by Esha Vishwakarma jadrn063 </h3>"));
		RootPanel.get().clear();
		RootPanel.get().add(main);
		board.setFocus(true);
		loadPanel();
	}


	private FlowPanel getButtonPanel(){
		FlowPanel p = new FlowPanel();
		Button clr = new Button("Clear");
		Button save = new Button("Save");
		Button load = new Button("Load");
		clr.setStyleName("my-button");
		save.setStyleName("my-button");
		load.setStyleName("my-button");

		//clears the whiteboard screen
		clr.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent e){
				board.setHTML("");
				status.setHTML("");
				}
		});
		//saves the whiteboard text
		save.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent e){
				savePanel();				
				}
		});
		
		//loads the text to whiteboard
		load.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent e){
				loadPanel();
				}
		});
		p.setStyleName("button-panel");
		p.add(clr);
		p.add(save);
		p.add(load);
		return p;
	}
	
		private void savePanel(){
		AsyncCallback callback = new AsyncCallback(){
			public void onSuccess(Object results){
				String answer = (String) results;
				if(answer.equals("OK")){
					status.setText("Whiteboard saved");					
					}
				else
					status.setText("ERROR, could not save contents");
				}
	
			public void onFailure(Throwable err){
				status.setText("Failed" + err.getMessage());
				err.printStackTrace();				
	        }
    };
		whiteboardService.save(board.getHTML(),tb.getText(),callback);
  }
  
  	private void loadPanel(){
		AsyncCallback callback = new AsyncCallback(){
			public void onSuccess(Object results){
				String answer = (String) results;
				board.setHTML(answer);
				status.setText("Whiteboard loaded from disk");			
				}
	
			public void onFailure(Throwable err){
				status.setText("Failed" + err.getMessage());
				err.printStackTrace();				
	        }
    };
		whiteboardService.load(callback);
  }
}