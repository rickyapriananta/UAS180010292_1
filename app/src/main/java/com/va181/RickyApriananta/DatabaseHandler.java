package com.va181.RickyApriananta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.constraintlayout.solver.ArrayRow;

import com.va181.RickyApriananta.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_AKTOR= "Aktor";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_SINOPSIS = "Sinopsis";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFromat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_AKTOR + " TEXT, "
                + KEY_GENRE+ " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());
        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL,  dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR,  dataFilm.getAktor ());
        cv.put(KEY_GENRE,  dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS,  dataFilm.getSinopsis ());
        cv.put(KEY_LINK,  dataFilm.getLink());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm ())});

        db.close();
    }

    public void  hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFromat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Film tempFilm = new Film (
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }

        return dataFilm;

    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return  location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //Menambah data film ke-1
        try {
            tempDate = sdFromat.parse("15/08/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Film film1 = new Film (
                idFilm,
                "DIGNITATE",
                tempDate,
                storeImageFile(R.drawable.film1),
                "Al Ghazali sebagai Alfi\n" +
                        "Caitlin Halderman sebagai Alana\n" +
                        "Giorgino Abraham sebagai Regan\n" +
                        "Teuku Ryzki sebagai Keenan\n" +
                        "Sophia Latjuba sebagai Mama Alfi dan Regan\n" +
                        "Izabel Jahja sebagai Mama Alana\n" +
                        "Budiman Sudjatmiko sebagai Papa Alfi dan Regan\n" +
                        "Sonia Alyssa sebagai Natasha\n" +
                        "Naimma Aljufri sebagai Clara\n" +
                        "Kiara McKenna sebagai Rana\n" +
                        "Lania Fira sebagai Sabitha\n" +
                        "Kenneth Santana sebagai Bevan\n" +
                        "Bramanta Sadhu sebagai Max\n" +
                        "Dinda Kanya Dewi sebagai Bu Ira\n" +
                        "Mat Oli sebagai Guru\n" +
                        "Ibnu Jamil sebagai Pak Satrio\n" +
                        "Arief Didu sebagai Kepala sekolah\n" +
                        "Lia Waode sebagai Marni\n" +
                        "Pio Kharisma sebagai Pak Tino\n" +
                        "Ade Habibie sebagai Bang Ali\n" +
                        "Fuad Baradja sebagai pengacara Regan\n" +
                        "Emmie Lemu sebagai Bu RT",
                "Film Drama Indonesia",
                "Alfi adalah seorang anak SMA yang galak dan dingin. Meski disukai banyak perempuan, ia tidak punya waktu untuk pacaran. Ia sangat serius dengan pelajaran di sekolahnya.\n" +
                        "\n" +
                        "Keenan, sahabatnya, berbanding terbalik dengannya. Ia selalu menyuruh Alfi agar lebih santai. Satu hari Alfi terlambat sekolah sehingga harus membersihkan toilet perempuan.Di sana ia bertemu dengan Alana, siswi baru yang cantik di sekolah. Ternyata keduanya satu kelas dan duduk bersebelahan. Bukannya akur, mereka malah sering berdebat karena sama-sama keras kepala. \n" +
                        "\n" +
                        "Alana tidak suka dengan kepribadian Alfi yang kasar. Hal itu disebabkan oleh masa lalu Alana.",
                "https://www.youtube.com/watch?v=v-94JfuzQAM"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data film ke-2
        try {
            tempDate = sdFromat.parse("25/01/2018 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film (
                idFilm,
                "Dilan 1990",
                tempDate,
                storeImageFile(R.drawable.film2),
                "Iqbaal Ramadhan sebagai Abdul Dilan\n" +
                        "Vanesha Prescilla sebagai Milea Adnan Hussain semasa SMA\n" +
                        "Sissy Priscillia sebagai Milea Adnan Hussain dewasa (narator)\n" +
                        "Giulio Parengkuan sebagai Anhar, teman geng Dilan.\n" +
                        "Omara Esteghlal sebagai Piyan, teman geng Dilan.\n" +
                        "Zulfa Maharani sebagai Rani, teman geng Milea.\n" +
                        "Yoriko Angeline sebagai Wati, teman geng Milea.\n" +
                        "Andryos Aryanto sebagai Nandan, teman geng Milea.\n" +
                        "Adhisty Zara (Zara JKT48) sebagai Disa, adik Dilan.\n" +
                        "Ira Wibowo sebagai bunda Dilan\n" +
                        "Farhan sebagai Kolonel Adnan, ayah Milea\n" +
                        "Happy Salma sebagai ibu Milea\n" +
                        "Brandon Salim sebagai Benni, pacar Milea di Jakarta.\n" +
                        "Teuku Rifnu Wikana sebagai Pak Suripto, guru BP.\n" +
                        "Refal Hady sebagai Kang Adi, guru les Milea.\n" +
                        "Moira Tabina Zayn (Moira) sebagai Airin, adik Milea.\n" +
                        "Gusti Rayhan sebagai Akew, sahabat Dilan.\n" +
                        "Yati Surachman sebagai Bi' Asih, tukang pijat.\n" +
                        "Tike Priatnakusumah sebagai Bi' E'em, penjaga kantin sekolah.\n" +
                        "Teddy Snada sebagai Kepala Sekolah\n" +
                        "Iang Darmawan sebagai Pak Rahmat\n" +
                        "Aris Nugraha sebagai Pak Atam\n" +
                        "Ridwan Kamil sebagai Pak Guru\n" +
                        "Joe Project P sebagai Mas Ato\n" +
                        "Ayu Inten sebagai Ibu Sri\n" +
                        "Ira Ratih sebagai Ibu Rini\n" +
                        "Polo Reza sebagai Burhan\n" +
                        "Ribka Uli Ozara Tambunan sebagai Susi\n" +
                        "Jubran Martawidjaja sebagai Agus\n" +
                        "Azzura Pinkania Imanda (Azzura Pinkan) sebagai Revi",
                "Drama Romantis",
                "Pada September 1990, Milea dan keluarganya pindah dari Jakarta ke Bandung. Saat hendak masuk di sebuah SMA, Milea bertemu dengan Dilan sang panglima geng motor. Dilan tak memperkenalkan dirinya, namun dengan sangat percaya diri segera meramal kalau Milea akan naik motor bersamanya dan menjadi pacarnya. Dilan, entah bagaimana caranya, mengetahui segala tentang Milea, bahkan alamat rumah dan nomor teleponnya. Singkat cerita, Dilan merayu-rayu Milea dengan memberikan berbagai hadiah yang bermakna, misalnya buku teka-teki silang yang sudah diisi supaya \"tidak perlu pusing karena harus mengisinya.\" Pada titik ini, Milea masih memiliki seorang pacar bernama Benni, yang ia tinggalkan secara fisik di Jakarta. Milea sendiri merasa tidak nyaman karena Benni adalah lelaki yang pencemburu dan kasar. Kepercayaan diri Dilan yang berlebih sempat membuat Nandan, sang sahabat yang juga menyukai Milea, tidak nyaman. Meski begitu, Milea mulai menyukai Dilan.\n" +
                        "\n" +
                        "Saat kelompok Milea maju ke lomba Cerdas Cermat antar sekolah yang dihelat di kantor pusat TVRI di Jakarta, tiba-tiba saja Benni muncul ke hadapan Milea. Milea, yang sedang makan berdua saja dengan Nandan karena ditinggal teman mereka yang pergi ke kamar mandi, terlibat cekcok dengan Benni yang mengira Nandan merusak hubungan asmaranya. Benni menghajar Nandan sebelum dilerai oleh Milea. Benni mengata-ngatai Milea dengan sebutan genit berkali-kali, sehingga Milea memutuskan hubungan mereka. Benni marah besar, melanjutkan makiannya dengan menyebut Milea \"pelacur\". Sekembalinya ke Bandung, Milea ditelepon Benni, yang kemudian memohon maaf. Milea sudah memaafkannya, namun menolak ajakan untuk kembali berpacaran. Benni mengeluarkan lagi makian \"setan\" dan \"pelacur\", yang dibalas Milea dengan menutup telepon. Setelahnya, hubungan Dilan dan Milea makin dekat saja, walau belum pernah ada kata cinta terucap. Mereka pulang sekolah berboncengan, sesekali bergandengan tangan, dan bertelepon malam-malam. Saking dekatnya, Milea berhasil membujuk Dilan supaya tidak lagi terlibat dalam tawuran antar geng.\n" +
                        "\n" +
                        "Milea juga membangun hubungan baik dengan bunda Dilan. Satu waktu, Milea ditunjuki kamar Dilan yang berantakan dan diajak merapikannya. Sang bunda juga menunjukkan puisi-puisi cinta yang dibuat Dilan untuk Milea.\n" +
                        "\n" +
                        "Milea harus menghadapi Kang Adi, mahasiswa yang merupakan guru les privatnya dan sang adik. Tampak bahwa Kang Adi menaksir Milea. Dia bahkan membawa Milea mengunjungi kampus tempatnya belajar, Institut Teknologi Bandung (ITB). Ketika Dilan mengetahui bahwa Milea pergi berdua dengan Kang Adi, ia mengirim puisi kekecewaan yang membuat Milea menyesal. Milea mencari Dilan ke rumahnya dan sekolah. Di sekolah ia bertemu Anhar, salah satu rekan geng Dilan, yang sedang mabuk. Anhar kemudian menamparnya. Setelah mengetahui peristiwa ini, Dilan menghajar Anhar habis-habisan. Mereka kemudian dilerai oleh guru BP dan kepala sekolah serta siswa-siswa lain. Mereka dan Milea pun dibawa ke ruang Kepala Sekolah. Setelah meninggalkan ruangan, Dilan dan Milea menuju warung Bi' E'em, di mana mereka resmi memulai hubungan pacaran.",
                "https://www.youtube.com/watch?v=X_b-wNkz4DU"
        );

        tambahFilm(film2, db);
        idFilm++;

        //Data film ke-3
        try {
            tempDate = sdFromat.parse("19/12/2019");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film (
                idFilm,
                "Imperfect",
                tempDate,
                storeImageFile(R.drawable.film3),
                "Jessica Mila sebagai Rara\n" +
                        "Reza Rahadian sebagai Dika\n"+
                        "Yasmin Napper sebagai Lulu\n"+
                        "Dion Wiyoko sebagai Kelvin\n"+
                        "Karina Suwandi sebagai Debby\n"+
                        "Kiki Narendra sebagai Hendro\n"+
                        "Shareefa Daanish sebagai Fey\n"+
                        "Dewi Irawan sebagai Ratih\n"+
                        "Ernest Prakasa sebagai Teddy\n"+
                        "Clara Bernadeth sebagai Marsha\n"+
                        "Boy William sebagai George\n"+
                        "Karina Nadila sebagai Irene\n"+
                        "Devina Aureel sebagai Wiwid\n"+
                        "Kiky Saputri sebagai Neti\n"+
                        "Zsazsa Utari sebagai Maria\n"+
                        "Aci Resti sebagai Prita\n"+
                        "Neneng Wulandari sebagai Endah\n"+
                        "Uus sebagai Ali\n"+
                        "Diah Permatasari sebagai Nora\n"+
                        "Wanda Hamidah sebagai Magda\n"+
                        "Olga Lydia sebagai Monik\n"+
                        "Asri Welas sebagai Siska\n"+
                        "Tutie Kirana sebagai Melinda\n"+
                        "Cathy Sharon sebagai Sheila",
                "Komedi",
                "Rara adalah anak perempuan yang memiliki hobi makan dalam jumlah besar, sehingga ia menjadi wanita bertubuh gemuk, berbanding terbalik dengan adiknya yang bertubuh kurus. Pada masa kecil, ayahnya tewas dalam kecelakaan di Tol Jagorawi.\n" +
                        "\n" +
                        "Setelah itu, rumahnya dijual ibunya dan keluarganya pindah ke rumah baru. Ketika besar, Rara bekerja di kantor yang dipimpin Kelvin. Rara sempat menjadi guru sukarela bagi anak-anak pemulung.  Di Malathi, perusahaan tempat ia bekerja, ia mendapatkan perilaku diskriminatif terkait dirinya yang bertubuh gemuk, termasuk Marsha dan dua kawannya, kecuali sahabatnya.\n" +
                        "\n" +
                        "Pada suatu hari, Sheila mengumumkan pengunduran diri. Rara diisi ke jabatan yang ditinggalkan. Perusahaan itu mengalami masalah keuangan, jadi Kelvin membutuhkan pengganti yang bisa mengatasi masalah itu\n" +
                        "\n" +
                        "Ia diminta menggunakan waktu selama sebulan untuk merampingkan tubuhnya. Walaupun terpaksa, ia menuruti permintaan atasannya.\n" +
                        "\n" +
                        "Ia melanjutkan sejumlah langkah diet hingga berjaya mengurangi beberapa puluh kilogram. Namun, perilakunya mulai berubah seiring beran tubuhnya. " +
                        "Ia akhirnya diterima dalam jaringan perkawanan Marsha, tetapi harus mengorbankan hubungan dengan kawan lamanya. Ia juga lebih memilih menggunakan taksi alih-alih motor, wapau akhirnya terlambat mengajari anak-anak pemulung.",
                "https://www.youtube.com/watch?v=f4Sc26vQHcU"
        );

        tambahFilm(film3, db);
        idFilm++;

        // Data film ke-4
        try {
            tempDate = sdFromat.parse("04/06/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film4 = new Film (
                idFilm,
                "Hit & Run",
                tempDate,
                storeImageFile(R.drawable.film4),
                "Joe Taslim sebagai Tegar\n" +
                        "Jefri Nichol sebagai Jefri\n" +
                        "Tatjana Saphira sebagai Meisa\n" +
                        "Yayan Ruhian sebagai Choki\n" +
                        "Chandra Liow sebagai Lio\n" +
                        "Nadya Arina sebagai Mila",
                "Komedi Laga",
                "Tegar Saputra, seorang polisi selebriti yang kemana-mana selalu diikuti kamera karena memiliki acara reality shownya sendiri. " +
                        "Tegar ditugaskan untuk menangkap Coki (Yayan Ruhian), seorang gembong narkoba yang baru kabur dari penjara. Sayangnya, di misi kali ini " +
                        "Tegar yang individualis harus dipasangkan dengan Lio, seorang tukang tipu. Tegar yang terbiasa beraksi sendirian kini harus berusaha menyelesaikan misinya bersama Lio yang justru membuat susah. " +
                        "Aksi Tegar dan Lio mencari Coki ditemanin Meisa seorang penyanyi dan Jefri.",
                "https://www.youtube.com/watch?v=M1bk-POU7yA"
        );

        tambahFilm(film4, db);
        idFilm++;

        //Data film ke-5
        try {
            tempDate = sdFromat.parse ( "28/03/2019" );
        } catch (ParseException er) {
            er.printStackTrace ();
        }
        Film film5 = new Film (
                idFilm,
                "My Stupid Boss2",
                tempDate,
                storeImageFile ( R.drawable.film5 ),
                "Reza Rahadian sebagai Bossman\n" +
                        "Bunga Citra Lestari sebagai Diana\n"+
                        "Kin Wah Chew sebagai Mr. Kho\n"+
                        "Atikah Suhaime sebagai Norahsikin\n"+
                        "Iedil Dzuhrie Alaudin sebagai Adrian\n"+
                        "Iskandar Zulkarnaen sebagai Azahari\n"+
                        "Alex Abbad sebagai Dika\n"+
                        "Melissa Karim sebagai Istri Bossman\n"+
                        "Morgan Oey sebagai Nguyen\n"+
                        "Verdi Solaiman sebagai Joni Wu\n"+
                        "Sahil Shah sebagai Babloo\n"+
                        "Prakash Krisna sebagai Raj\n"+
                        "Indra Jegel sebagai Faisal\n"+
                        "Awek Sitepu sebagai Rahmat\n"+
                        "Gregory Prabowo sebagai MC Konferensi",
                "Komedii",
                "Film ini merupakan lanjutan dari film My Stupid Boss yang pertama rilis pada tahun 2016. Film bergenre komedi yang menceritakan seorang Bossman (Reza Rahardian) yang terkenal yang selalu membangga-banggaka dirinya, pelit dan perhitungan pada karyawan yang bekerja di pabrik miliknya." +
                        "Banyak karyawan yang jengkel dengan sifat si Bossman yang pelit dan perhitungan. Salah satunya Faisal (Indra Jegel), ia meminta mesin kayu baru kepada Diana (Bunga Citra Lestari) dan Diana melapor ke Bossman agar dibelikan mesin kayu yang baru, tetapi hal itu ditolak oleh Bossman karena mesin kayu nya mahal. " +
                        "Selepas Faisal dan teman-teman nya pergi dan pamitan, Mr.Kho (Chew Kin Wah) akhirnya lapor ke Diana untuk berbicara ke Bossman beberapa karyawan nya mengundurkan diri dan pegawai nya terlihat kebingungan karena tidak ada yang mau bekerja lagi di pabrik dan akan beresiko bangkrut.",
                "https://www.youtube.com/watch?v=aM-HV2GI2qw"
        );

        tambahFilm (film5,db );

    }
}

