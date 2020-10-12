package com.example.wise.announcement

import com.example.wise.updatesystem.FAB
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty


class NoteProperty (
        var noteTitle: SimpleStringProperty = SimpleStringProperty(),
        var noteBody: SimpleStringProperty = SimpleStringProperty(),
        var factory: SimpleObjectProperty<FAB> = SimpleObjectProperty(),
        var type: SimpleStringProperty = SimpleStringProperty()
)



