package ru.talicamed.uzi_utilites;

import java.util.Scanner;

/**
 * Запускаемый класс программы для обработки DICOM-объектов (изображений), переданных с УЗИ аппрата.
 * Используется с УЗИ-сканером Mindray DC-7, с другими аппратами не испытывался.
 * Сервер, слушающий DICOM-порт, запускается в отдельном потоке.
 * Использует файл настроек (ini-файл))
 * TODO: Если не хватает прав для биндинга к порту, серверный поток выкидывает BindException, но не завершается.
 * TODO: Принтер не подключен. Подключить.
 *
 * <b>Зависимости</b>: pixelmed, PixelMed™ Java DICOM Toolkit, http://www.pixelmed.com/
 *
 * @author Roman Orekhov, tripsin@yandex.ru
 * @version 0.1 alpha
 * @since 2016-11-01
 */

public class Main {

    /** Выводит в консоль справку по использованию и завершает программу.
     * TODO: Написать справку
     */
    private static void help(){
        System.err.println("PRINTING HELP");
        System.exit(0);
    }

    /** Главный запускаемый метод */
    public static void main(String[] args) {
        System.err.println("DICOM Image Receiver - v0.1 alpha");
        System.err.println("=======================================");

        DicomProperties properties = null;

        if (args.length == 0) help();
        switch (args[0]) {
            case "-h": help(); break;
            case "-d": properties = DicomProperties.loadDefaults(); break;
            case "-c": if (args.length > 1) properties = DicomProperties.load(args[1]);
                else properties = DicomProperties.load(DicomProperties.DEFAULT_CONFIG_FILE_NAME); break;
            case "-n": if (args.length > 1) properties = DicomProperties.create(args[1]);
                else properties = DicomProperties.create(DicomProperties.DEFAULT_CONFIG_FILE_NAME); break;
            default: help();
        }

        DicomImageConverter converter = new DicomImageConverter(properties);

        DicomImageReceiver.Go(properties, converter);

        //TODO: create printer

        System.err.println("Press Enter to stop ...");
        new Scanner(System.in).nextLine();
        System.exit(0);
    }
}
