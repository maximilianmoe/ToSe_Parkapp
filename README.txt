
#Datenbank Bilder:

create table Bilder (photoid int(6) auto_increment, path varchar(100), file_name varchar(100), primary key (photoid));

alter table Bilder add foreign key (pid) references Parkplatz (pid);


#Hinweise:
-Bilder sollten klein sein - da schau ich noch um einen Workaround (ein Testbild "test" mit 80kb liegt im static.. )
-man muss nen ordner "photos" auf C:(bzw. root Verzeichnis des Betriebsystems) erstellen, da es den nicht automatisch
erstellt, da werden dann die Bilder abgespeichert. Auf dem Server ist das dann eh egal, da wirds dann anders gemapped..
