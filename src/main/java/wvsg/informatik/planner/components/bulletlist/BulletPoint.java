package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.TagEntity;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
/**
 * Diese Klasse repräsentiert einen einzelnen Bullet Point in einer Bullet-Liste.
 */
public class BulletPoint extends HorizontalLayout implements Comparable<BulletPoint>, ObjectWithSetType {
    //region Data
    protected UUID bulletId;
    protected String content = "Lorem ipsum";
    protected int type = 0;
    protected int position = 0;
    protected int indent = 0;
    protected boolean isCompleted = false;
    protected TagEntity[] tags;
    protected Date createdAt;

    protected BulletList owner;
    //endregion

    private String previousContent = "Empty Filler エンプティー";

    //region Setter
    /**
     * Setzt den Inhalt des Bullet Points.
     *
     * @param content Der neue Inhalt
     */
    public void setContent(String content){
        this.content = content;
        owner.bulletRepository.updateBulletContent(bulletId, content);
        redrawContent();
    }
    /**
     * Setzt den Typ des Bullet Points.
     *
     * @param type Der neue Typ als String
     */
    @Override
    public void setType(int type){
        this.type = type;

        owner.bulletRepository.updateBulletType(bulletId, BulletPoint.parseTypeToString(type));
        redrawButton();
    }
    /**
     * Setzt den Typ des Bullet Points.
     *
     * @param typeString Der neue Typ als Ganzzahl
     */
    @Override
    public void setType(String typeString){
        int type = BulletPoint.parseStringToType(typeString);
        this.type = type;

        owner.bulletRepository.updateBulletType(bulletId, typeString);
        redrawButton();
    }
    /**
     * Setzt den Einzug des Bullet Points.
     *
     * @param indent Der neue Einzug
     */
    public void setIndent(int indent){
        if(indent >= 0){
            this.indent = indent;
            owner.bulletRepository.updateBulletIndent(bulletId, indent);
            redrawIndent();
        }
    }
    /**
     * Setzt den Status des Bullet Points (abgeschlossen oder nicht).
     *
     * @param isCompleted Der neue Status
     */
    public void setCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
        owner.bulletRepository.bulletIsCompleted(bulletId, isCompleted);

        redrawCheckbox();
    }
    /**
     * Setzt die Position des Bullet Points und zeichnet die Liste neu.
     *
     * @param position Die neue Position
     */
    public void setPosition(int position){
        setPositionWithoutRedraw(position);

        owner.redrawPositions();
    }
    /**
     * Setzt die Position des Bullet Points, ohne die Liste neu zu zeichnen.
     *
     * @param position Die neue Position
     */
    public void setPositionWithoutRedraw(int position){
        setPositionWithoutDatabase(position);
        updatePositionInDatabase();
    }
    /**
     * Setzt die Position des Bullet Points, ohne die Datenbank zu aktualisieren.
     *
     * @param position Die neue Position
     */
    public void setPositionWithoutDatabase(int position){
        this.position = position;
        positionText.setText(Integer.toString(position));
    }
    /**
     * Aktualisiert die Position des Bullet Points in der Datenbank.
     */
    public void updatePositionInDatabase(){
        owner.bulletRepository.updateBulletPosition(bulletId, position);
    }
    //endregion

    //region dataContainer
    protected TextField dataText;
    protected HorizontalLayout indentContainer;
    protected HorizontalLayout bulletContentLayout;
    protected Button typeButton;
    protected Checkbox checkbox;
    protected Dialog typeSelector;
    protected Checkbox isSelectedCheckbox;
    protected Text positionText;
    boolean showPosition = false;
    //endregion

    public BulletPoint(BulletEntity bulletEntity, BulletList owner){
        this.bulletContentLayout = new HorizontalLayout();
        this.bulletContentLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Width.FULL);
        this.bulletContentLayout.getStyle().set("gap", "0");
        this.addClassNames(LumoUtility.JustifyContent.BETWEEN, LumoUtility.AlignItems.CENTER, LumoUtility.Width.FULL);

        this.owner = owner;

        addIndentContainer();
        addButtonContainer();
        addDataContainer();
        add(bulletContentLayout);
        if(!owner.getReadOnlyList()){
            addSelectionCheckbox();
        }

        setData(bulletEntity);

        positionText = new Text(Integer.toString(position));
        if(showPosition){
            add(positionText);
        }
    }
    /**
     * Lädt die Daten des Bullet Points aus der Datenbank.
     *
     * @param bulletEntity Das BulletEntity-Objekt, das die Daten enthält
     */

    //region Load data from database
    public void setData(BulletEntity bulletEntity){
        this.bulletId = bulletEntity.getId();
        this.content = bulletEntity.getContent();
        this.type = parseStringToType(bulletEntity.getType());
        this.indent = bulletEntity.getIndent();
        this.position = bulletEntity.getPosition();
        this.isCompleted = bulletEntity.getIsCompleted();
        this.tags = bulletEntity.getTags();
        this.createdAt = bulletEntity.getCreatedAt();

        redrawAll();
    }
    //endregion

    //region updating component values
    public void redrawAll(){
        redrawContent();
        redrawButton();
        redrawIndent();
    }
    //region updating component values
    /**
     * Zeichnet alle Komponenten des Bullet Points neu.
     */
    public void redrawContent(){
        dataText.setValue(this.content);
    }
    /**
     * Zeichnet den Inhalt des Bullet Points neu.
     */
    public void redrawButton(){
        redrawButtonType();
        redrawCheckbox();

        typeButton.setVisible(true);
        checkbox.setVisible(false);

        if(type == 2){
            typeButton.setVisible(false);
            checkbox.setVisible(true);
        }
    }
    /**
     * Zeichnet den Typ-Button des Bullet Points neu.
     */
    public void redrawButtonType(){
        typeButton.setIcon(getIconByType(type));
    }

    /**
     * Zeichnet die Checkbox des Bullet Points neu.
     */
    public void redrawCheckbox(){
        checkbox.setValue(isCompleted);
    }

    /**
     * Zeichnet den Einzug des Bullet Points neu.
     */
    public void redrawIndent(){
        indentContainer.removeAll();
        indentContainer.add(new Span());
        for(int i = 0; i<indent; i++){
            indentContainer.add(new Span());
        }
    }
    //endregion
    //region add empty container
    /**
     * Fügt den Container für den Button hinzu.
     */
    protected void addButtonContainer(){
        Dialog selector = createTypeSelector(this);
        this.typeSelector = selector;

        Button button = new Button();
        button.addClassName("bulletpoint-button");
        this.typeButton = button;

        Checkbox checkbox = new Checkbox("", (e) -> setCompleted(e.getValue()));
        checkbox.addClassNames(LumoUtility.AlignSelf.CENTER, LumoUtility.Padding.Horizontal.SMALL);
        this.checkbox = checkbox;

        button.setVisible(true);
        checkbox.setVisible(false);

        if(type == 2){
            button.setVisible(false);
            checkbox.setVisible(true);
        }

        if(!owner.getReadOnlyList()){
            button.addClickListener(e -> typeSelector.open());
        }else{
            checkbox.setReadOnly(true);
        }

        this.bulletContentLayout.add(selector, button);
        this.bulletContentLayout.add(checkbox);
    }
    /**
     * Fügt den Container für den Einzug hinzu.
     */
    protected void addIndentContainer(){
        HorizontalLayout indents = new HorizontalLayout();
        this.indentContainer = indents;

        this.bulletContentLayout.add(indents);
    }
    /**
     * Fügt den Container für den Inhalt hinzu.
     */
    protected void addDataContainer(){
        TextField text = new TextField();
        text.setClassName(LumoUtility.Width.FULL);
        text.getStyle().set("background-color", "transparent");
        text.addClassName("bulletpoint-textfield");
        text.setValueChangeMode(ValueChangeMode.EAGER);

        text.addValueChangeListener(e -> {
            previousContent = e.getOldValue();
            setContent(onValueChange(e.getValue()));
        });
        text.addKeyDownListener(Key.BACKSPACE, e -> {
            if(previousContent.equals(text.getValue())){
                setIndent(indent-1);
            }
            previousContent = text.getValue();
        });
        text.addKeyPressListener(Key.TAB, e -> setIndent(indent + 1));
        text.addKeyPressListener(Key.ENTER, e -> text.blur());

        if(owner.getReadOnlyList()){
            text.setReadOnly(true);
        }

        this.dataText = text;
        this.bulletContentLayout.add(text);
    }
    /**
     * Fügt die Checkbox für die Auswahl hinzu.
     */
    protected void addSelectionCheckbox(){
        Checkbox c = new Checkbox();

        c.addValueChangeListener(e -> {
            if(e.getValue()){
                owner.bulletEditor.selectBullet(this);
            }else{
                owner.bulletEditor.deselectBullet(this);
            }
        });

        this.isSelectedCheckbox = c;
        add(c);
    }
    //endregion
    /**
     * Wird aufgerufen, wenn sich der Inhalt des Textfelds ändert.
     * Führt Aktionen entsprechend des neuen Werts aus.
     *
     * @param newValue Der neue Wert des Textfelds
     * @return Der bearbeitete Wert des Textfelds
     */

    protected String onValueChange(String newValue){
        if(newValue.length() == 2){
            if(testTextshortcuts(newValue)){return "";}
        }else if(newValue.length() > 2){
            if(testTextshortcuts(newValue.substring(0, 2))){return newValue.substring(2);}
        }
        return newValue;
    }
    /**
     * Testet Text-Shortcuts und führt entsprechende Aktionen aus.
     *
     * @param s Der zu testende Text
     * @return <code>true</code>, wenn ein Shortcut erkannt wurde, ansonsten <code>false</code>
     */

    protected boolean testTextshortcuts(String s){
        switch (s) {
            case "- " -> setType(0);
            case ". " -> setType(1);
            case "[ " -> setType(2);
            case "* " -> setType(3);
            case "  " -> setIndent(indent + 1);
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "BulletPoint{" +
                "content='" + content + '\'' +
                '}';
    }

    @Override
    public int compareTo(BulletPoint bulletPoint) {
        return Integer.compare(this.position, bulletPoint.position);
    }
    /**
     * Erstellt den Dialog zur Auswahl des Typs.
     *
     * @param s Der Bullet Point, für den der Dialog erstellt wird
     * @return Der erstellte Dialog
     */

    public static Dialog createTypeSelector(ObjectWithSetType s){
        Dialog selector = new Dialog();

        Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE), (e) -> selector.close());
        cancelButton.setClassName(LumoUtility.Margin.Left.XLARGE);

        selector.getHeader().add(new H2("Select a bullet type"));
        selector.getHeader().add(cancelButton);

        VerticalLayout selectorLayout = new VerticalLayout();
        selectorLayout.setClassName(LumoUtility.Padding.NONE);

        selectorLayout.add(new Button("Dash", getIconByType(0), e -> {s.setType(0); selector.close();}));
        selectorLayout.add(new Button("Bullet", getIconByType(1), e -> {s.setType(1); selector.close();}));
        selectorLayout.add(new Button("To-Do", getIconByType(2), e -> {s.setType(2); selector.close();}));
        selectorLayout.add(new Button("Star", getIconByType(3), e -> {s.setType(3); selector.close();}));

        selector.add(selectorLayout);

        return selector;
    }
    /**
     * Gibt das Icon für einen bestimmten Typ zurück.
     *
     * @param type Der Typ als Ganzzahl
     * @return Das entsprechende Icon
     */
    protected static Component getIconByType(int type){
        Component c;

        if(type == 0){c = new Icon(VaadinIcon.MINUS);
        }else if(type == 1){c = new Icon(VaadinIcon.CIRCLE);
        }else if(type == 2){c = new Icon(VaadinIcon.CHECK_SQUARE);
        }else if(type == 3){c = new Icon(VaadinIcon.STAR);
        }else{c = LineAwesomeIcon.PAW_SOLID.create();}

        return c;
    }
    //region type-parsing
    protected static final Map<String, Integer> typeDictionary = Map.of("Dash", 0, "Bullet", 1, "Checkbox", 2, "Star", 3);

    /**
     * Wandelt einen Typ-String in einen Typ-Ganzzahl um.
     *
     * @param type Der Typ als String
     * @return Der Typ als Ganzzahl
     */
    protected static int parseStringToType(String type){
        Integer p = typeDictionary.get(type);
        if(p != null){return p;}

        System.out.println("\"" + type + "\" did not match any type!");
        return 0;
    }

    /**
     * Wandelt einen Typ-Ganzzahl in einen Typ-String um.
     *
     * @param type Der Typ als Ganzzahl
     * @return Der Typ als String
     */
    protected static String parseTypeToString(int type){
        for (Map.Entry<String,Integer> i: typeDictionary.entrySet()) {
            if(i.getValue() == type){
                return i.getKey();
            }
        }
        return "Null";
    }
    //endregion
}

