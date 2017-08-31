All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

*   JDBC drivers are test compile, not compile dependencies.
*   Logging SQL statements in log level _fine_.
*   Derive `JDBCTableViewBuilder` to configure table columns.

## [2.0.0] - 2017-08-29

### Added

*   Documentation.

### Changed

*   Refine the API.
*   No use of unchecked exceptions. The `SQLException` must be handled, wrap by
    an unchecked exception is futile.
*   JavaFX table view is not extended, it is configured.

## 1.0.0 - 2017-08-27

### Added

*   JDBC facade to with a fluent API.
*   Use of unchecked SQL exception. 
*   Extend result set with Java 8 Date Time API.
*   Extend result set with single result object mapping.
*   Extend result set with result list object mapping.
*   Extends JavaFX table view using JDBC result set as data source.
 

[Unreleased]: https://github.com/falkoschumann/java-jdbc-facade/compare/v2.0.0...HEAD
[2.0.0]: https://github.com/falkoschumann/java-jdbc-facade/compare/v1.0.0...v2.0.0
