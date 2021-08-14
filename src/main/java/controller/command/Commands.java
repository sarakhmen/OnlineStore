package controller.command;

public enum Commands {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    SIGNUP(new SignupCommand()),
    MAINVIEW(new CatalogViewCommand());

    private final Command command;

    public Command getCommand(){
        return command;
    }

    Commands(Command command){
        this.command = command;
    }
}
