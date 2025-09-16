package wvsg.informatik.planner.components.bulletlist;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse ListListDialog erweitert die Dialog-Komponente und stellt ein Dialogfenster zum Verschieben von Bullet Points zwischen Listen dar.
 */
public class ListListDialog extends Dialog {

    BulletList bulletList;

    /**
     * Die RadioButtonGroup zur Auswahl der Ziel-Listen.
     */
    public RadioButtonGroup<String> userLists;

    /**
     * Konstruktor für den ListListDialog.
     *
     * @param bulletList Die BulletList, zu der der Dialog gehört
     */
    public ListListDialog(BulletList bulletList) {
        this.bulletList = bulletList;
        addContainer();
        redrawListList();
    }

    /**
     * Fügt den Container zum Dialogfenster hinzu.
     */
    public void addContainer() {
        H2 dialogTitle = new H2("Your Lists");
        dialogTitle.addClassName(LumoUtility.Padding.Top.SMALL);
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE),
                (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(dialogTitle, closeButton);
        headerLayout.addClassNames(LumoUtility.JustifyContent.BETWEEN, LumoUtility.AlignItems.START,
                LumoUtility.Padding.NONE, LumoUtility.Width.FULL);
        getHeader().add(headerLayout);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        this.userLists = radioGroup;
        add(radioGroup);

        Button confirmButton = new Button("Confirm", e -> moveBulletPoint());
        confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getFooter().add(confirmButton);
    }

    /**
     * Aktualisiert die Liste der verfügbaren Ziel-Listen.
     */
    public void redrawListList() {
        UserEntity currentUser = bulletList.dataService.getCurrentlyAuthenticatedUserEntity();
        List<ListEntity> listList = bulletList.listRepository.getUserLists(currentUser);

        List<String> names = new ArrayList<>();
        for (ListEntity l : listList) {
            names.add(l.getTitle());
        }
        names.remove(bulletList.getTitle());
        userLists.setItems(names);
    }

    /**
     * Überschreibt die Methode open, um die Liste der Ziel-Listen vor dem Öffnen des Dialogs zu aktualisieren.
     */
    @Override
    public void open() {
        redrawListList();
        super.open();
    }

    /**
     * Verschiebt den ausgewählten Bullet Point in die Ziel-Liste.
     */
    public void moveBulletPoint() {
        if (userLists.getValue() == null) {
            return;
        }
        bulletList.bulletEditor.moveBulletsTo(bulletList.listRepository.findByTitle(userLists.getValue()).getId());
    }
}
