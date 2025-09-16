package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.ListEntity;

import java.util.*;

/**
 * Die Klasse BulletEditor repräsentiert den Dialog zur Bearbeitung von BulletPoints.
 */
public class BulletEditor extends Dialog implements ObjectWithSetType{

    private List<BulletPoint> selectedBullets;

    /**
     * Wählt einen BulletPoint aus und fügt ihn zur Liste der ausgewählten BulletPoints hinzu.
     *
     * @param b der ausgewählte BulletPoint
     */
    public void selectBullet(BulletPoint b){
        if(!selectedBullets.contains(b)){
            selectedBullets.add(b);
        }
        if(selectedBullets.size() == 1){
            open();
        }
        Collections.sort(selectedBullets);
    }

    /**
     * Entfernt einen ausgewählten BulletPoint aus der Liste der ausgewählten BulletPoints.
     *
     * @param b der abgewählte BulletPoint
     */
    public void deselectBullet(BulletPoint b){
        selectedBullets.remove(b);
        if(selectedBullets.size() == 0){
            close();
        }

        deselectAllButton.setVisible(false);
        selectAllButton.setVisible(true);
    }

    private BulletList bulletList;

    private Dialog typeSelectorDialog;
    private Dialog changeListDialog;
    private Button deselectAllButton;
    private Button selectAllButton;

    /**
     * Erstellt einen neuen BulletEditor für eine BulletList.
     *
     * @param bulletList die BulletList, zu der der BulletEditor gehört
     */
    public BulletEditor(BulletList bulletList){
        this.addClassName("bullet-editor");

        this.bulletList = bulletList;
        selectedBullets = new ArrayList<>();

        setModal(false);
        setDraggable(true);

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deselectAllButton = new Button("Deselect all", e -> deselectAll());
        deselectAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(deselectAllButton);
        this.deselectAllButton = deselectAllButton;
        deselectAllButton.setVisible(false);

        Button selectAllButton = new Button("Select all", e ->{
            for(BulletPoint b : bulletList.bullets){
                b.isSelectedCheckbox.setValue(true);
            }
            e.getSource().setVisible(false);
            deselectAllButton.setVisible(true);
        });
        selectAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(selectAllButton);
        this.selectAllButton = selectAllButton;

        this.typeSelectorDialog = BulletPoint.createTypeSelector(this);
        Button changeTypeButton = new Button(new Icon("lumo", "unordered-list"), e -> this.typeSelectorDialog.open());
        changeTypeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(changeTypeButton);

        Button moveSelectedDownButton = new Button(new Icon("lumo", "angle-up"), e -> moveSelectedDown());
        moveSelectedDownButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(moveSelectedDownButton);

        Button moveSelectedUpButton = new Button(new Icon("lumo", "angle-down"), e -> moveSelectedUp());
        moveSelectedUpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(moveSelectedUpButton);

        Button removeIndentButton = new Button(new Icon("vaadin", "deindent"), e -> removeIndent());
        removeIndentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(removeIndentButton);

        Button addIndentButton = new Button(new Icon("vaadin", "indent"), e -> addIndent());
        addIndentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(addIndentButton);

        this.changeListDialog = new ListListDialog(bulletList);
        Button changeListButton = new Button("Move to another list", e -> this.changeListDialog.open());
        changeListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(changeListButton);


        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete selected Bullets?");
        dialog.setCancelable(true);

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> deleteSelectedBullets());
        add(dialog);

        Button deleteButton = new Button(new Icon("vaadin", "trash"), e -> dialog.open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(deleteButton);
    }

    /**
     * Löscht die ausgewählten BulletPoints aus der BulletList.
     */
    public void deleteSelectedBullets(){
        for(BulletPoint b : selectedBullets){
            bulletList.bulletRepository.removeBulletById(b.bulletId);
            bulletList.bullets.remove(b);
        }
        bulletList.sortBulletPoints();
        bulletList.fixPositions();
        bulletList.redrawBullets();
        selectedBullets.clear();
        close();
    }

    @Override
    public void setType(int type) {
        for(BulletPoint b : selectedBullets){
            b.setType(type);
        }
    }

    @Override
    public void setType(String typeString) {
        for(BulletPoint b : selectedBullets){
            b.setType(typeString);
        }
    }

    /**
     * Verschiebt die ausgewählten BulletPoints in eine andere Liste.
     *
     * @param listId die ID der Liste, in die die BulletPoints verschoben werden sollen
     */
    public void moveBulletsTo(UUID listId){
        ListEntity moveTo = bulletList.listRepository.getReferenceById(listId);
        List<BulletEntity> bs = bulletList.bulletRepository.findBulletsByList(moveTo);

        int counter = 0;
        for(BulletPoint b : selectedBullets){
            counter++;
            b.setPositionWithoutRedraw(bs.size()+counter);
            bulletList.bulletRepository.updateBulletsListId(b.bulletId, moveTo);
        }
        UI.getCurrent().getPage().reload();
    }

    /**
     * Hebt die Auswahl aller BulletPoints auf.
     */
    public void deselectAll(){
        while(selectedBullets.size() != 0){
            selectedBullets.get(0).isSelectedCheckbox.setValue(false);
        }
    }

    /**
     * Verschiebt die ausgewählten BulletPoints nach oben.
     */
    public void moveSelectedUp(){
        int size = selectedBullets.size();

        if(size != 0){
            for(int i = size-1; i >= 0; i--){
                BulletPoint b = selectedBullets.get(i);
                int index = bulletList.bullets.indexOf(b);

                if(index < bulletList.bullets.size()-1){
                    b.setPositionWithoutDatabase(b.position + 1);

                    BulletPoint previous = bulletList.bullets.get(index + 1);
                    previous.setPositionWithoutDatabase(previous.position - 1);

                    Collections.swap(bulletList.bullets, index, index + 1);
                }
            }

            Collections.sort(selectedBullets);
            bulletList.redrawBullets();

            //updating BulletPoint.position in database
            BulletPoint lastSelectedBullet = selectedBullets.get(size-1);
            BulletPoint firstSelectedBullet = selectedBullets.get(0);

            int from = bulletList.bullets.indexOf(firstSelectedBullet) - 1;
            int to = bulletList.bullets.indexOf(lastSelectedBullet);
            updateBulletPositionsInDatabase(from, to);
        }
    }

    /**
     * Verschiebt die ausgewählten BulletPoints nach unten.
     */
    public void moveSelectedDown(){
        int size = selectedBullets.size();

        if(size != 0){
            for (BulletPoint b : selectedBullets) {
                int index = bulletList.bullets.indexOf(b);

                if (index > 0) {
                    b.setPositionWithoutDatabase(b.position - 1);

                    BulletPoint previous = bulletList.bullets.get(index - 1);
                    previous.setPositionWithoutDatabase(previous.position + 1);

                    Collections.swap(bulletList.bullets, index, index - 1);
                }
            }

            Collections.sort(selectedBullets);
            bulletList.redrawBullets();

            //updating BulletPoint.position in database
            BulletPoint lastSelectedBullet = selectedBullets.get(size-1);
            BulletPoint firstSelectedBullet = selectedBullets.get(0);

            int from = bulletList.bullets.indexOf(firstSelectedBullet);
            int to = bulletList.bullets.indexOf(lastSelectedBullet) + 1;
            updateBulletPositionsInDatabase(from, to);
        }
    }

    /**
     * Erhöht den Einzug der ausgewählten BulletPoints.
     */
    public void addIndent(){
        for(BulletPoint b : selectedBullets){
            b.setIndent(b.indent + 1);
        }
    }

    /**
     * Verringert den Einzug der ausgewählten BulletPoints.
     */
    public void removeIndent(){
        for(BulletPoint b : selectedBullets){
            b.setIndent(b.indent - 1);
        }
    }

    /**
     * Aktualisiert die Positionen der BulletPoints in der Datenbank.
     *
     * @param from die Ausgangsposition
     * @param to   die Endposition
     */
    public void updateBulletPositionsInDatabase(int from, int to){
        for(int i = from; i <= to; i++){
            bulletList.bullets.get(i).updatePositionInDatabase();
        }
    }
}
