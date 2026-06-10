import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BibliotecaInteligentePro extends JFrame {

    // Contadores
    private int usuarios = 0;
    private int prestamos = 0;
    private int reservas = 0;
    private int multas = 0;

    // Etiquetas estadísticas
    private JLabel lblUsuarios;
    private JLabel lblPrestamos;
    private JLabel lblReservas;
    private JLabel lblMultas;

    // Tablas
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloHistorial;

    // Componentes usuarios
    private JTextField txtIdUsuario;
    private JTextField txtNombreUsuario;

    // Componentes libros
    private JTextField txtCodigoLibro;
    private JTextField txtTituloLibro;
    private JTextField txtCantidadLibro;

    // Préstamos
    private JComboBox<String> cbUsuarios;
    private JComboBox<String> cbLibros;

    public BibliotecaInteligentePro() {

        setTitle("Biblioteca Inteligente - Agente Reactivo");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "📚 BIBLIOTECA INTELIGENTE - AGENTE REACTIVO",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        add(titulo, BorderLayout.NORTH);

        // Panel estadísticas
        JPanel estadisticas = new JPanel(new GridLayout(1,4));

        lblUsuarios = new JLabel("Usuarios: 0",SwingConstants.CENTER);
        lblPrestamos = new JLabel("Préstamos: 0",SwingConstants.CENTER);
        lblReservas = new JLabel("Reservas: 0",SwingConstants.CENTER);
        lblMultas = new JLabel("Multas: 0",SwingConstants.CENTER);

        estadisticas.add(lblUsuarios);
        estadisticas.add(lblPrestamos);
        estadisticas.add(lblReservas);
        estadisticas.add(lblMultas);

        add(estadisticas, BorderLayout.SOUTH);

        JTabbedPane pestañas = new JTabbedPane();

        pestañas.add("Usuarios", crearPanelUsuarios());
        pestañas.add("Libros", crearPanelLibros());
        pestañas.add("Préstamos", crearPanelPrestamos());
        pestañas.add("Historial", crearPanelHistorial());

        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelUsuarios() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(3,2));

        txtIdUsuario = new JTextField();
        txtNombreUsuario = new JTextField();

        formulario.add(new JLabel("ID Usuario"));
        formulario.add(txtIdUsuario);

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombreUsuario);

        JButton btnRegistrar = new JButton("Registrar Usuario");

        formulario.add(btnRegistrar);

        panel.add(formulario, BorderLayout.NORTH);

        modeloUsuarios = new DefaultTableModel(
                new String[]{"ID","Nombre"},0);

        JTable tabla = new JTable(modeloUsuarios);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarUsuario());

        return panel;
    }

    private JPanel crearPanelLibros() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(4,2));

        txtCodigoLibro = new JTextField();
        txtTituloLibro = new JTextField();
        txtCantidadLibro = new JTextField();

        formulario.add(new JLabel("Código"));
        formulario.add(txtCodigoLibro);

        formulario.add(new JLabel("Título"));
        formulario.add(txtTituloLibro);

        formulario.add(new JLabel("Cantidad"));
        formulario.add(txtCantidadLibro);

        JButton btnAgregar = new JButton("Agregar Libro");

        formulario.add(btnAgregar);

        panel.add(formulario, BorderLayout.NORTH);

        modeloLibros = new DefaultTableModel(
                new String[]{"Código","Título","Cantidad"},0);

        JTable tabla = new JTable(modeloLibros);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarLibro());

        return panel;
    }

    private JPanel crearPanelPrestamos() {

        JPanel panel = new JPanel(new GridLayout(5,1));

        cbUsuarios = new JComboBox<>();
        cbLibros = new JComboBox<>();

        JButton btnPrestamo = new JButton("Realizar Préstamo");

        panel.add(new JLabel("Usuario"));
        panel.add(cbUsuarios);

        panel.add(new JLabel("Libro"));
        panel.add(cbLibros);

        panel.add(btnPrestamo);

        btnPrestamo.addActionListener(e -> realizarPrestamo());

        return panel;
    }

    private JPanel crearPanelHistorial() {

        JPanel panel = new JPanel(new BorderLayout());

        modeloHistorial = new DefaultTableModel(
                new String[]{"Percepción","Acción"},0);

        JTable tabla = new JTable(modeloHistorial);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        return panel;
    }

    // ==========================
    // AGENTE REACTIVO
    // ==========================

    private void registrarUsuario() {

        String id = txtIdUsuario.getText();
        String nombre = txtNombreUsuario.getText();

        if(id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Complete los datos");
            return;
        }

        modeloUsuarios.addRow(new Object[]{id,nombre});

        cbUsuarios.addItem(nombre);

        usuarios++;

        lblUsuarios.setText("Usuarios: " + usuarios);

        registrarEvento(
                "usuario_nuevo",
                "Registrar usuario");

        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
    }

    private void agregarLibro() {

        String codigo = txtCodigoLibro.getText();
        String titulo = txtTituloLibro.getText();
        String cantidad = txtCantidadLibro.getText();

        if(codigo.isEmpty() ||
           titulo.isEmpty() ||
           cantidad.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Complete los datos");
            return;
        }

        modeloLibros.addRow(
                new Object[]{
                        codigo,
                        titulo,
                        Integer.parseInt(cantidad)
                });

        cbLibros.addItem(titulo);

        registrarEvento(
                "libro_registrado",
                "Agregar libro");

        txtCodigoLibro.setText("");
        txtTituloLibro.setText("");
        txtCantidadLibro.setText("");
    }

    private void realizarPrestamo() {

        if(cbLibros.getSelectedIndex()==-1) {
            return;
        }

        String libro =
                cbLibros.getSelectedItem().toString();

        for(int i=0;i<modeloLibros.getRowCount();i++) {

            String titulo =
                    modeloLibros.getValueAt(i,1).toString();

            if(titulo.equals(libro)) {

                int cantidad =
                        Integer.parseInt(
                                modeloLibros
                                .getValueAt(i,2)
                                .toString());

                // REGLA DEL AGENTE

                if(cantidad > 0) {

                    cantidad--;

                    modeloLibros.setValueAt(
                            cantidad,
                            i,
                            2);

                    prestamos++;

                    lblPrestamos.setText(
                            "Préstamos: " + prestamos);

                    registrarEvento(
                            "libro_disponible",
                            "Prestar libro");

                    JOptionPane.showMessageDialog(
                            this,
                            "Préstamo realizado");

                } else {

                    reservas++;

                    lblReservas.setText(
                            "Reservas: " + reservas);

                    registrarEvento(
                            "libro_no_disponible",
                            "Reservar libro");

                    JOptionPane.showMessageDialog(
                            this,
                            "Libro reservado");

                }

                return;
            }
        }
    }

    private void registrarEvento(
            String percepcion,
            String accion) {

        modeloHistorial.addRow(
                new Object[]{
                        percepcion,
                        accion
                });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new BibliotecaInteligentePro()
                    .setVisible(true);

        });
    }
}