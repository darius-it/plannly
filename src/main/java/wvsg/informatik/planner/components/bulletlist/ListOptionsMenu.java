package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.repository.BulletRepository;
import wvsg.informatik.planner.data.repository.ListRepository;

/**
 * Die Klasse ListOptionsMenu erweitert die MenuBar-Komponente und stellt das Optionsmenü für Listen dar.
 */
public class ListOptionsMenu extends MenuBar {
    ListRepository listRepository;
    ListEntity currentList;
    BulletRepository bulletRepository;
    String newListTitle;
    TextField newListNameInput;

    /**
     * Konstruktor für das ListOptionsMenu.
     *
     * @param currentList      Die aktuelle ListEntity
     * @param listRepository   Das ListRepository
     * @param bulletRepository Das BulletRepository
     */
    public ListOptionsMenu(ListEntity currentList, ListRepository listRepository, BulletRepository bulletRepository) {
        this.currentList = currentList;
        this.listRepository = listRepository;
        this.bulletRepository = bulletRepository;

        ConfirmDialog deleteListDialog = new ConfirmDialog();
        deleteListDialog.setHeader("Are you sure you want to delete this list?");
        deleteListDialog.setCancelable(true);
        deleteListDialog.setConfirmText("Delete");
        deleteListDialog.setConfirmButtonTheme("error primary");
        deleteListDialog.addConfirmListener(event -> deleteList());
        UI.getCurrent().add(deleteListDialog);

        Dialog listRenameDialog = getListRenameDialog();
        UI.getCurrent().add(listRenameDialog);

        Dialog shareListDialog = getShareListDialog();
        UI.getCurrent().add(shareListDialog);

        MenuItem item = this.addItem(new Icon(VaadinIcon.ELLIPSIS_DOTS_H));
        item.addClassNames(LumoUtility.Background.TRANSPARENT);
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Share", e -> shareListDialog.open());
        subMenu.addItem("Collaborate", e -> Notification.show("Coming soon"));
        subMenu.add(new Hr());
        subMenu.addItem("Rename list", e -> {
            listRenameDialog.open();
            newListNameInput.focus();
        });
        subMenu.addItem("Delete list", e -> deleteListDialog.open());
    }

    /**
     * Löscht die aktuelle Liste und alle zugehörigen Bullet Points.
     */
    public void deleteList() {
        bulletRepository.deleteAllBulletsFromList(currentList);
        listRepository.deleteById(currentList.getId());

        UI.getCurrent().navigate("/home");
        UI.getCurrent().getPage().reload();
    }

    /**
     * Gibt einen Dialog zurück, um den Namen der Liste zu ändern.
     *
     * @return Der Dialog zum Umbenennen der Liste
     */
    public Dialog getListRenameDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Rename list");

        TextField newListNameTextField = new TextField("New list name");
        newListNameTextField.setValue(currentList.getTitle());
        newListNameTextField.setClassName(LumoUtility.Padding.Top.NONE);
        newListNameTextField.addValueChangeListener(e ->  {
            if (!dialog.isOpened()) return;
            this.newListTitle = e.getValue();
        });
        newListNameTextField.setPlaceholder("Enter a list name...");
        newListNameTextField.addClassName(LumoUtility.Margin.Right.XLARGE);
        this.newListNameInput = newListNameTextField;
        dialog.add(newListNameTextField);

        Button saveButton = new Button("Save", e -> {
            if (!dialog.isOpened()) return;
            listRepository.updateListTitle(currentList.getId(), this.newListTitle);
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return dialog;
    }

    /**
     * Gibt einen Dialog zurück, um die Liste freizugeben.
     *
     * @return Der Dialog zum Freigeben der Liste
     */
    public Dialog getShareListDialog(){
        Dialog dialog = new Dialog();

        Button makePublicButton = new Button("");
        makePublicButton.addClickListener(e -> {
            Boolean b = !currentList.isPublic();
            listRepository.updateListIsPublic(currentList.getId(), b);
            if(b){
                makePublicButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                makePublicButton.setText("Make private");
            }else{
                makePublicButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                makePublicButton.setText("Make public");
            }
        });
        Boolean b = !currentList.isPublic();
        if(b){
            makePublicButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            makePublicButton.setText("Make private");
        }else{
            makePublicButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            makePublicButton.setText("Make public");
        }
        dialog.add(makePublicButton);

        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        dialog.getHeader().add(cancelButton);

        return dialog;
    }
}
