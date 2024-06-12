package org.example.enuns;

public enum Competencias {
        PYTHON ("Python"),
        C_SHARP ("C#"),
        C_PLUS_PLUS ("C++"),
        JS ("JS"),
        PHP("PHP"),
        SWIFT("Swift"),
        JAVA("Java"),
        GO("Go"),
        SQL("SQl"),
        Ruby("Rubi"),
        HTML("HTML"),
        CSS("Css"),
        NOSQL("NOSQL"),
        FLUTTER("Flutter"),
        TYPESCRIPT("TypeScript"),
        PERL("Perl"),
        COBOL("Cobol"),
        DOTNET("dotNet"),
        KOTLIN("Kotlin"),
        DART("Dart");

    private String competencia;

    Competencias(String competencia) {
        this.competencia = competencia;
    }

    public String getCompetencia() {
        return competencia;
    }
}
