package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.data.value.ValueChangeMode;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.ListEntity;

/**
 * Die Klasse BulletPointCreator erbt von der Klasse BulletPoint und stellt einen Bullet Point-Ersteller dar.
 * Dieser Bullet Point-Ersteller ermöglicht das Hinzufügen neuer Bullet Points zur Liste.
 */
public class BulletPointCreator extends BulletPoint {

    /**
     * Konstruktor für den BulletPointCreator.
     *
     * @param bulletEntity Der BulletEntity des Bullet Points
     * @param owner        Die Besitzerliste des Bullet Points
     */
    public BulletPointCreator(BulletEntity bulletEntity, BulletList owner) {
        super(bulletEntity, owner);
        dataText.setValueChangeMode(ValueChangeMode.ON_BLUR);
        dataText.setPlaceholder("Type here to create a new bullet...");
    }

    /**
     * Setzt den Inhalt des Bullet Points und fügt ihn der Liste hinzu.
     * Wenn der Inhalt nicht leer ist, wird ein neuer Bullet Point zur Liste hinzugefügt.
     * Anschließend wird der Inhalt zurückgesetzt und der Bullet Point neu gezeichnet.
     *
     * @param content Der Inhalt des Bullet Points
     */
    @Override
    public void setContent(String content) {
        this.content = content;
        if (!content.equals("")) {
            owner.addBulletPoint(toNewBulletEntity(this, owner.listEntity));

            this.content = "";
            redrawContent();
        }
    }

    /**
     * Setzt den Typ des Bullet Points und aktualisiert die Darstellung des Buttons.
     *
     * @param type Der Typ des Bullet Points
     */
    @Override
    public void setType(int type) {
        this.type = type;
        redrawButton();
    }

    /**
     * Setzt den Einzug des Bullet Points und fügt ihn der Liste hinzu.
     * Wenn der Einzug größer oder gleich 0 ist, wird ein neuer Bullet Point zur Liste hinzugefügt.
     * Anschließend wird der Einzug zurückgesetzt und der Bullet Point neu gezeichnet.
     *
     * @param indent Der Einzug des Bullet Points
     */
    @Override
    public void setIndent(int indent) {
        this.indent = indent;
        if (indent >= 0) {
            owner.addBulletPoint(toNewBulletEntity(this, owner.listEntity));

            this.indent = 0;
            redrawIndent();
        }
    }

    /**
     * Setzt den Status des Bullet Points (abgeschlossen oder nicht) und fügt ihn der Liste hinzu.
     * Anschließend wird der Status auf nicht abgeschlossen zurückgesetzt und der Bullet Point neu gezeichnet.
     *
     * @param isCompleted Der Status des Bullet Points
     */
    @Override
    public void setCompleted(boolean isCompleted) {
        owner.addBulletPoint(toNewBulletEntity(this, owner.listEntity));

        this.isCompleted = false;
        redrawCheckbox();
    }

    /**
     * Überschreibt die Methode addSelectionCheckbox, um die Checkbox für die Auswahl nicht hinzuzufügen.
     */
    @Override
    protected void addSelectionCheckbox() {
    }

    /**
     * Fügt den Datencontainer hinzu und konfiguriert das Textfeld.
     * Das Textfeld erhält einen KeyPress-Listener, um den Fokus beizubehalten, wenn die Eingabetaste gedrückt wird.
     */
    @Override
    protected void addDataContainer() {
        super.addDataContainer();
        this.dataText.addKeyPressListener(Key.ENTER, e -> this.dataText.focus());
    }

    /**
     * Konvertiert den BulletPointCreator in einen neuen BulletEntity.
     *
     * @param b           Der BulletPointCreator
     * @param listEntity  Die ListEntity der Liste
     * @return            Der neue BulletEntity
     */
    public static BulletEntity toNewBulletEntity(BulletPointCreator b, ListEntity listEntity) {
        return new BulletEntity(b.content, parseTypeToString(b.type), b.owner.bullets.size(), b.indent, b.isCompleted, listEntity.getOwner(), listEntity);
    }
}
