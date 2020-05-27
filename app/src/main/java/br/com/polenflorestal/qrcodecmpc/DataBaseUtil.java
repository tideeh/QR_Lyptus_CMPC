package br.com.polenflorestal.qrcodecmpc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseUtil {
    private static DataBaseUtil INSTANCE = null;
    private static String NOME_DB = "qrcode_polen_db";
    private static SQLiteDatabase db;

    public static void abrir(Context ctx){
        db = MyApp.context.openOrCreateDatabase(NOME_DB, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    public static long inserir(String tabela, ContentValues valores){
        long id = db.insert(tabela, null, valores);
        Log.i("BANCO_DADOS", "Cadastrou registro com o id "+id);
        return id;
    }

    public static int atualizar(String tabela, ContentValues valores, String where){
        int count = db.update(tabela, valores, where, null);
        Log.i("BANCO_DADOS", "Atualizou "+count+" registros");
        return count;
    }

    public static int deletar(String tabela, String where){
        int count = db.delete(tabela, where, null);
        Log.i("BANCO_DADOS", "Deletou "+count+" registros");
        return count;
    }

    public static Cursor buscar(String tabela, String[] colunas, String where, String orderBy){
        Cursor c;

        if(!where.equals("")){
            c = db.query(tabela, colunas, where,null,null,null, orderBy);
        }
        else {
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        }

        Log.i("BANCO_DADOS","Realizou uma busca e retornou [" + c.getCount() + "] registros.");

        return c;
    }

    public static void fechar(){
        if (db != null) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }

    public static void criaDB(){
        String[] SCRIPT_DATABASE_CREATE = {
                "DROP TABLE IF EXISTS Arvore;",
                "DROP TABLE IF EXISTS Comentario;",

                "CREATE TABLE [Arvore] ([codigo] Text, [tipo] Integer, [local] Text, [parcela] Integer, [linha] Integer, [bloco] Integer, [arvore_pos] Integer, [codigo_geno] Text, [genitor_fem] Text, [genitor_mas] Text, [data_plantio] Text, [ult_medicao] Text, [dap] Integer, [altura] Integer, [vol] Float, [procedencia] Text, [historico] Text, [especie_comp] Text);",

                "CREATE TABLE Comentario (arvore_codigo TEXT, comentario TEXT, data TEXT);",

                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P587B87A3', 0, 'Viçosa/MG', 587, 99, 87, 3, 'DG X U2', '(E. dunnii x E. grandis) x (E. urophylla 2)', 'Desconhecido (pol. Livre)', '2/18/2007 12:00:00 AM', '5/20/2019 12:00:00 AM', 20, 22, 0.18, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P2089B338A1', 1, 'Viçosa/MG', 2089, 14, 338, 39, 'Paubrasilia echinata ', '(E. dunnii x E. grandis) x (E. urophylla 2)', 'Desconhecido (pol. Livre)', '8/27/2011 12:00:00 AM', '5/21/2019 12:00:00 AM', 21, 23, 0.19, 'Mariana/MG', 'Este indivíduo foi resgatado de uma área afetada por rejeito de barragens no ano de 2015. Utilizando-se técnicas de enxertia, propágulos foram colhidos da árvore atingida e enxertados em porta-enxerto da mesma espécie, no município de Viçosa/MG. Após 6 meses de cuidados em viveiro, a muda enxertada foi reintroduzida ná área de onde foi resgatada, garantindo a permanência do indivíduo em seu local de ocorrência natural, e permitindo a perpetuação dos genes desta árvore na região.', 'Lam.');",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P4361B729A5', 0, 'Viçosa/MG', 4361, 37, 729, 5, 'U1 X U2', '(E. urophylla) x (E. urophylla)', 'desconhecido (pol. Livre)', '3/20/2008 12:00:00 AM', '5/22/2019 12:00:00 AM', 22, 24, 0.2, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P6722B1200A61', 0, 'Viçosa/MG', 6722, 49, 1200, 61, 'U1 X U2', '(E. urophylla) x (E. urophylla)', 'desconhecido (pol. Livre)', '8/24/2003 12:00:00 AM', '5/23/2019 12:00:00 AM', 23, 25, 0.21, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P1040B156A103', 0, 'Viçosa/MG', 1040, 20, 156, 87, 'DG X U2', '(E. dunnii x E. grandis) x (E. urophylla 2)', 'desconhecido (pol. Livre)', '7/13/2017 12:00:00 AM', '5/24/2019 12:00:00 AM', 24, 26, 0.22, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P4769B805A1', 1, 'Viçosa/MG', 4769, 39, 805, 9, 'Apuleia leiocarpa ', '(E. dunnii x E. grandis) x (E. urophylla 2)', 'desconhecido (pol. Livre)', '7/14/2017 12:00:00 AM', '5/25/2019 12:00:00 AM', 25, 27, 0.23, 'Vale do Rio Doce/MG', 'Esta árvore ocorre do Pará ao Rio Grande do Sul na Floresta latifoliada semidecidua, e do sul da Bahia ao Espírito Santo na Floresta Pluvial Atlântica. Sua casca produz tanino, muito utilizado para curtir couro.\nEste indivíduo é proveniente de sementes colhidas em área de mineração, tendo sido produzido em parceria com viveiristas locais integrantes da rede de sementes e mudas de nossa empresa. Com isso, conseguimos aliar a conservação genética de espécies nativas com o incentivo ao desenvolvimento socioambiental das áreas onde a empresa opera.', '(Vogel)');",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P7092B1276A4', 0, 'Viçosa/MG', 7092, 51, 1276, 44, 'DG X C1 ', '(E. dunnii x E. grandis) x (E. camaldulensis)', 'desconhecido (pol. Livre)', '7/15/2017 12:00:00 AM', '5/26/2019 12:00:00 AM', 26, 28, 0.24, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P2085B337A2', 0, 'Viçosa/MG', 2085, 25, 337, 2, 'G1 X UGL', '(E. grandis) x (E. urophylla x E. globulus)', 'desconhecido (pol. Livre)', '7/16/2017 12:00:00 AM', '5/27/2019 12:00:00 AM', 27, 29, 0.25, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P1342B201A7', 0, 'Viçosa/MG', 1342, 22, 201, 7, 'G1 X UGL', '(E. grandis) x (E. urophylla x E. globulus)', 'desconhecido (pol. Livre)', '7/17/2017 12:00:00 AM', '5/28/2019 12:00:00 AM', 28, 30, 0.26, null, null, null);",
                "INSERT INTO [Arvore]([codigo], [tipo], [local], [parcela], [linha], [bloco], [arvore_pos], [codigo_geno], [genitor_fem], [genitor_mas], [data_plantio], [ult_medicao], [dap], [altura], [vol], [procedencia], [historico], [especie_comp]) VALUES('P4899B828A6', 0, 'Viçosa/MG', 4899, 40, 828, 6, 'DG X UGL', '(E. dunnii x E. grandis) x (E. urophylla x E. globulus)', 'desconhecido (pol. Livre)', '7/18/2017 12:00:00 AM', '5/29/2019 12:00:00 AM', 29, 31, 0.27, null, null, null);"

                //"INSERT INTO [Comentario]([arvore_codigo], [comentario], [data]) VALUES('P4899B828A6', 'TESTE AAAAAAAAAAAAAAAAAAA B', '7/18/2017 12:00:00 AM');",
                //"INSERT INTO [Comentario]([arvore_codigo], [comentario], [data]) VALUES('P4899B828A6', 'TESTE CCCCCCCCCCCCC D', '11/02/2018');"
        };

        for (String s : SCRIPT_DATABASE_CREATE) {
            db.execSQL(s);
        }

        Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
    }
}
