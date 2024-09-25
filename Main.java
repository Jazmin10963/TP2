import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<String> frases = new ArrayList<>();

    public static void main(String[] args) {
        cargarFrasesDesdeFichero();
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Te doy la bienvenida! ¿Qué deseas hacer?");
        while (true) {
            System.out.println("Selecciona una opción:");
            System.out.println("1 - Agregar una nueva frase");
            System.out.println("2 - Editar una frase");
            System.out.println("3 - Salir");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    agregarNuevaFrase(scanner);
                    break;
                case "2":
                    editarFrase(scanner);
                    break;
                case "3":
                    imprimirMensajeLento("Saliendo...", 250);
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
            try {
                Thread.sleep(1000); // 1 segundo de retraso
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error en el retraso: " + e.getMessage());
            }
        }
    }

    private static void cargarFrasesDesdeFichero() {
        try (BufferedReader reader = new BufferedReader(new FileReader("frases.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                frases.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el fichero: " + e.getMessage());
        }
    }

    private static void procesarFrase(Scanner scanner, boolean esEdicion, int index) {
        System.out.println("Por favor, ingresa un texto con no más de 140 caracteres:");
        String texto = scanner.nextLine();

        if (texto.length() > 140) {
            System.out.println("El texto ingresado tiene más de 140 caracteres.");
        } else if (texto.isEmpty()) {
            System.out.println("El texto ingresado está vacío.");
        } else {
            int[] counts = contarPalabrasVocalesConsonantes(texto);
            System.out.println("Cantidad de palabras: " + counts[0]);
            System.out.println("Cantidad de vocales: " + counts[1]);
            System.out.println("Cantidad de consonantes: " + counts[2]);

            if (esEdicion) {
                frases.set(index, texto);
                System.out.println("Frase actualizada.");
            } else {
                frases.add(texto);
                System.out.println("Frase guardada.");
            }
            generarFichero();
            System.out.println("Total de frases: " + frases.size());
        }
    }

    private static void agregarNuevaFrase(Scanner scanner) {
        procesarFrase(scanner, false, -1);
    }

    private static void editarFrase(Scanner scanner) {
        System.out.println("Listado de frases:");
        for (int i = 0; i < frases.size(); i++) {
            System.out.println((i + 1) + ". " + frases.get(i));
        }
        System.out.println("Selecciona el número de la frase que deseas editar:");
        String input = scanner.nextLine();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < frases.size()) {
                procesarFrase(scanner, true, index);
            } else {
                System.out.println("Número de frase no válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida. Debe ser un número.");
        }
    }

    private static int[] contarPalabrasVocalesConsonantes(String texto) {
        int cantPalabras = texto.split("\\s+").length;
        int cantVocales = 0;
        int cantConsonantes = 0;

        for (char c : texto.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiouáéíóú".indexOf(c) != -1) {
                    cantVocales++;
                } else {
                    cantConsonantes++;
                }
            }
        }
        return new int[]{cantPalabras, cantVocales, cantConsonantes};
    }

    private static void generarFichero() {
        try (FileWriter writer = new FileWriter("frases.txt")) {
            for (String frase : frases) {
                writer.write(frase + "\n");
            }
            System.out.println("Fichero generado.");
        } catch (IOException e) {
            System.out.println("Error al generar el fichero: " + e.getMessage());
        }
    }

    private static void imprimirMensajeLento(String mensaje, int retraso) {
        for (char c : mensaje.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(retraso);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error en la impresión lenta: " + e.getMessage());
            }
        }
    }
}