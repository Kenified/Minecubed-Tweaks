package com.minecubedmc.modules.Ranking;

import java.io.Serializable;

public record Match(GlickoPlayer opponent, double result) implements Serializable {}


