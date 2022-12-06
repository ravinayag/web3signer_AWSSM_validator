/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.signers.bls.keystore;

import org.apache.tuweni.bytes.Bytes;
import tech.pegasys.signers.bls.keystore.model.KeyStoreData;

import java.io.Console;
import java.nio.file.Path;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DecryptKeystore {
    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("Usage: java -cp *.jar DecryptKeystore.java <path_to_keystore>");
            System.exit(0);
        }

        try {
            final KeyStoreData keyStoreData = KeyStoreLoader.loadFromFile(Path.of(args[0]));
            final String password = askPassword();
            if (password == null) {
                System.err.println("Password is required");
                System.exit(1);
            }
            final Bytes decryptedKey = KeyStore.decrypt(password, keyStoreData);
            System.out.println(decryptedKey.toHexString());
            System.exit(0);
        } catch (final Exception e) {
            System.err.println("Decryption failed: " + e.getMessage());
            System.exit(-1);
        }
    }

    private static String askPassword() {
        final String password;
        final Console console = System.console();
        if (console != null) {
            final char[] passwordFromConsole = console.readPassword("Enter Password:");
            password = passwordFromConsole == null ? null : new String(passwordFromConsole);
        } else {
            Scanner scanner = new Scanner(System.in, UTF_8.name());
            password = scanner.nextLine();
        }

       return password;
    }
}
