[![Build Status](https://travis-ci.org/falkoschumann/java-jdbc-facade.svg?branch=master)](https://travis-ci.org/falkoschumann/java-jdbc-facade)
[![GitHub release](https://img.shields.io/github/release/falkoschumann/java-jdbc-facade.svg)]()

JDBC Facade
===========

A fluent API as facade for JDBC.

JDBC statements can be build by an fluent API. The used result set is extended
for getting objects from the Java 8 Time API. Also a result set can mapped to an
single object or a list of objects.  

A JavaFX table view can be used to dynamically display data from an JDBC result
set.


Installation
------------

### Gradle

Add the the repository _jcenter_ to your `build.gradle`

    repositories {
        jcenter()
    }

and add the dependency

    compile 'de.muspellheim:jdbc-facade:2.0.0'


### Maven

Add the the repository _jcenter_ to your `pom.xml`
    
    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>central</id>
            <name>bintray</name>
            <url>http://jcenter.bintray.com</url>
        </repository>
    </repositories>

and add the dependency

    <dependencies>
        <dependency>
            <groupId>de.muspellheim</groupId>
            <artifactId>jdbc-facade</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>


### Download

You can download JARs with binary, source and JavaDoc from GitHub under
https://github.com/falkoschumann/java-jdbc-facade/releases.


Usage
-----

### Data Definition Language: CREATE, ALTER, DROP

    DataSource dataSource = ...
    JDBCFacade jdbc = new JDBCFacade(dataSource);
    jdbc.executeSQLCommand(connection -> connection.statement("DROP SCHEMA IF EXISTS my_schema").execute());
    jdbc.executeSQLCommand(connection -> connection.statement("CREATE SCHEMA my_schema").execute());
    jdbc.executeSQLCommand(connection -> connection.statement("CREATE TABLE my_table (...)").execute());

### Data Manipulation Language: INSERT, UPDATE, DELETE, SELECT

#### Execute Updates

Insert some values

    jdbc.executeSQLCommand(connection -> connection.preparedStatement(
        "INSERT INTO warengruppe (warengruppe_id, bezeichnung) "
            + "VALUES "
            + "(1, 'Bürobedarf'),"
            + "(2, 'Pflanzen'),"
            + "(3, 'Gartenbedarf'),"
            + "(4, 'Werkzeug')")
        .executeUpdate());

or with parameters
        
    jdbc.executeSQLCommand(connection -> {
        PreparedStatementBuilder statement = connection.preparedStatement(
            "INSERT INTO artikel_nm_warengruppe SET artikel_id=?, warengruppe_id=?");
        statement.withParam(3001).withParam(1).executeUpdate();
        statement.withParam(3005).withParam(1).executeUpdate();
        statement.withParam(3006).withParam(1).executeUpdate();
        statement.withParam(3007).withParam(1).executeUpdate();
        statement.withParam(3010).withParam(1).executeUpdate();
        statement.withParam(7856).withParam(2).executeUpdate();
        statement.withParam(7856).withParam(3).executeUpdate();
        statement.withParam(7863).withParam(2).executeUpdate();
        statement.withParam(7863).withParam(3).executeUpdate();
        statement.withParam(9010).withParam(3).executeUpdate();
        statement.withParam(9010).withParam(4).executeUpdate();
        statement.withParam(9015).withParam(3).executeUpdate();
        statement.withParam(9015).withParam(4).executeUpdate();
    });

A SQL statement with parameters can initialize one and execute multiple. After
execution you can set the parameter with new values and execute the same
statement. 

Of course you can select some data. E.g. a single result find by primary key 

    Optional<Artikel> artikel = jdbc.executeSQLQuery(connection -> connection.preparedStatement(
        "SELECT * FROM artikel WHERE artikel_id = ?")
        .withParam(3010)
        .executeQuery()
        .getSingleResult(ArtikelMapper::map));

or a result list with multiple objects

    List<Artikel> artikel = jdbc.executeSQLQuery(connection -> connection.preparedStatement(
         "SELECT * FROM artikel WHERE einzelpreis BETWEEN ? AND ?")
         .withParam(1.00)
         .withParam(15.00)
         .executeQuery()
         .getResultList(ArtikelMapper::map));

And here the mapping function

    public class ArtikelMapper {

        public static Artikel map(ExtendedResultSet resultSet) throws SQLException {
            Artikel artikel = new Artikel();
            artikel.setId(resultSet.getInt("artikel_id"));
            artikel.setBezeichnung(resultSet.getString("bezeichnung"));
            artikel.setEinzelpreis(Money.of(resultSet.getDouble("einzelpreis"), resultSet.getString("waehrung")));
            artikel.setDeleted(resultSet.getBoolean("deleted"));
            return artikel;
        }

    }


### JavaFX Table View

    DataSource dataSource = ...
    TableView tableView = new TableView();
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT * FROM my_table";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        new JDBCTableViewBuilder(tableView).initWithResultSet(resultSet);
    } catch (SQLException ex) {
        // handle exception
    }


Contributing
------------

### Publish artifacts to Bintray

1.  Create file `gradle.properties` and set properties `bintrayUser` and
    `bintrayApiKey`.
2.  Run `./gradlew uploadArchives`.
3.  Check uploaded files and publish.

### Publish distribution to GitHub

1.  Run `./gradle distZip`.
2.  Upload created ZIP to GitHub releases.

