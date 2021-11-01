package foobar;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.source.spi.AttributePath;

import java.util.Locale;

public class SnakeCaseNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    /**
     Utilizado mesma implementação de hibernate 4(jpa 1) <br>
     vide {@link org.hibernate.cfg.DefaultComponentSafeNamingStrategy}
     **/
    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        AttributePath attributePath = source.getAttributePath();
        String columnName = addUnderscores(attributePath.getProperty());
        return toIdentifier(columnName, source.getBuildingContext());
    }

    /**
     Utilizado mesma implementação de hibernate 4(jpa 1) <br>
     vide {@link org.hibernate.cfg.DefaultComponentSafeNamingStrategy}
     **/
    private static String addUnderscores(String name) {
        StringBuilder builder = new StringBuilder( name.replace('.', '_') );
        for (int i=1; i<builder.length()-1; i++) {
            if (Character.isLowerCase( builder.charAt(i-1) )
                    && Character.isUpperCase( builder.charAt(i) )
                    && Character.isLowerCase( builder.charAt(i+1) )) {
                builder.insert(i++, '_');
            }
        }
        return builder.toString().toUpperCase(Locale.ROOT);
    }
}
