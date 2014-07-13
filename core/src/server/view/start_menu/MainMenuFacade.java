package server.view.start_menu;

public class MainMenuFacade {
    
    // Private constructor prevents instantiation from other classes
    private MainMenuFacade() { }
    private BattleDriver myDriver;

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder { 
            public static final MainMenuFacade INSTANCE = new MainMenuFacade();
    }

    public static void addDriver(BattleDriver d){
    	SingletonHolder.INSTANCE.myDriver=d;
    }
    
    public static void showInstructions(){
    	   SingletonHolder.INSTANCE.myDriver.showInstructions();
    }

    public static void showGameSetup(){
    	MainMenuFacade facade=SingletonHolder.INSTANCE;
    	facade.myDriver.setVisible(false);
    	facade.myDriver.showGameSetup();
    }

    public static void endGame(){
    	MainMenuFacade facade=SingletonHolder.INSTANCE;
    	facade.myDriver.setVisible(false);
		System.exit(1);
    }
}
