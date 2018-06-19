package com.shindygo.shindy.main.model;

import com.shindygo.shindy.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenreDataFactory {

    public static List<Genre> makeGenres() {
        return Arrays.asList(makeRockGenre(),
                makeRockGenre(),
                makeRockGenre(),
                makeRockGenre(),
                makeRockGenre(), makeRockGenre(), makeRockGenre());
    }


    public static Genre makeRockGenre() {
        return new Genre("Rock", makeRockArtists(), R.drawable.calender_star);
    }


    public static List<Artist> makeRockArtists() {
        Artist profie = new Artist("Profile", true);
//        Artist favorite = new Artist("Favorite", true);
//        Artist invite = new Artist("Invite", true);
//        Artist message = new Artist("Message", true);
        return Arrays.asList(profie);
    }

    public static Genre makeJazzGenre() {
        return new Genre("Jazz", makeJazzArtists(), R.drawable.camera);
    }


    public static List<Artist> makeJazzArtists() {
        Artist milesDavis = new Artist("Miles Davis", true);

        return Collections.singletonList(milesDavis);
    }

    public static Genre makeClassicGenre() {
        return new Genre("Classic", makeClassicArtists(), R.drawable.cross);
    }


    public static List<Artist> makeClassicArtists() {
        Artist beethoven = new Artist("Ludwig van Beethoven", false);

        return Collections.singletonList(beethoven);
    }

    public static Genre makeSalsaGenre() {
        return new Genre("Salsa", makeSalsaArtists(), R.drawable.heart_black);
    }


    public static List<Artist> makeSalsaArtists() {
        Artist hectorLavoe = new Artist("Hector Lavoe", true);

        return Arrays.asList(hectorLavoe);
    }

    public static Genre makeBluegrassGenre() {
        return new Genre("Bluegrass", makeBluegrassArtists(), R.drawable.heart_white);
    }


    public static List<Artist> makeBluegrassArtists() {
        Artist billMonroe = new Artist("Bill Monroe", false);
        Artist earlScruggs = new Artist("Earl Scruggs", false);
        Artist osborneBrothers = new Artist("Osborne Brothers", true);
        Artist johnHartford = new Artist("John Hartford", false);

        return Arrays.asList(billMonroe, earlScruggs, osborneBrothers, johnHartford);
    }

}
