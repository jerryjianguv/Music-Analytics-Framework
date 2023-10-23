package cs.cmu.cs214.framework.gui;

public class Plugin {
    private final String name;
    public Plugin(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return """
            {
                "name" : "%s"
            }
            """.formatted(this.name);
    }
}
