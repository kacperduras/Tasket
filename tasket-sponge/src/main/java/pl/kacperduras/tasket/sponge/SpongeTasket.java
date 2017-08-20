package pl.kacperduras.tasket.sponge;

import org.apache.commons.lang3.Validate;
import org.spongepowered.api.Game;
import pl.kacperduras.tasket.Tasket;

public final class SpongeTasket extends Tasket {

    private final Game game;

    public SpongeTasket(Game game) {
        Validate.notNull(game);

        this.game = game;
    }

}
