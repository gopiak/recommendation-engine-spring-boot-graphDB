package com.github.webdevgopi.slash.models.graphQL;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlashGraphQlResultArtist {
    private GraphQlDataArtist data;
    private Object extensions;
}
