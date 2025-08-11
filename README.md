# ModSuite

**ModSuite** est un plugin de modération complet pour serveurs Minecraft, offrant un large panel d’outils aux administrateurs et modérateurs.  
Il est conçu pour être **modulaire**, **multilingue**, avec un système de **permissions personnalisées**, une **intégration Discord**, un support **base de données**, et la possibilité d’utiliser des **couleurs en hexadécimal** pour la personnalisation des messages.

> **État du projet** : 🚧 **WIP** – En cours de développement, certaines fonctionnalités peuvent être incomplètes ou sujettes à modifications.

---

## ✨ Fonctionnalités principales

- 🛡️ **Outils de modération complets** : warn, ban, tempban, mute, freeze, vanish, maintenance, etc.
- 🌍 **Système de langues** (ex. `fr-FR`) configurable.
- 🎨 **Personnalisation des messages** avec codes couleurs **hexadécimaux** (`#RRGGBB`).
- 📜 **Historique des sanctions** et commandes de lookup.
- 🔐 **Permissions personnalisées** pour un contrôle granulaire.
- 🔔 **Système d’alertes Discord** via Webhook
- ![alt text](https://github.com/TwitmundDev/ModSuite-Public/blob/master/ressources/Capture%20d'%C3%A9cran%202025-08-11%20171931.png "image exemple")
- 🧊 **Freeze avec son activable**.
- 🧠 **Détection d’alts** avec actions configurables : notify, ban, banip, tempban, kick.

---

## ⚙️ Fichier `config.yml`

```yaml
lang: "fr-FR"
maintenance_status: false
discord_webhook_url: "https://discord.com/api/webhooks/..."
discord_webhook_enabled: true
enable_freeze_sound: true

# --- ALTS CONFIGURATION ---
punishment_alts_type: "notify"
punishment_alts_time: 3600
```

---

## 🔧 Options personnalisables

| Option                     | Description |
|----------------------------|-------------|
| **lang**                   | Code de langue (ex. `fr-FR`, `en-US`). |
| **maintenance_status**     | Active/désactive le mode maintenance (`true` / `false`). |
| **discord_webhook_url**    | URL du webhook Discord où envoyer les alertes. |
| **discord_webhook_enabled**| Active ou non l’envoi des alertes Discord. |
| **enable_freeze_sound**    | Joue un son lorsqu’un joueur est freeze. |
| **punishment_alts_type**   | Action sur les comptes alternatifs (`ban`, `banip`, `tempban`, `kick`, `notify`). |
| **punishment_alts_time**   | Durée de sanction pour les alts (en secondes). |

---

## 📜 Commandes

| Commande       | Description |
|----------------|-------------|
| `/warn`        | Avertir un joueur. |
| `/ban`         | Bannir un joueur. |
| `/unban`       | Débannir un joueur. |
| `/tempban`     | Bannir temporairement un joueur. |
| `/maintenance` | Activer/désactiver le mode maintenance. |
| `/mute`        | Rendre muet un joueur. |
| `/unmute`      | Rétablir la parole d’un joueur. |
| `/lookup`      | Consulter l’historique des sanctions. |
| `/staff`       | Activer/désactiver le mode staff. |
| `/freeze`      | Geler un joueur. |
| `/vanish`      | Activer/désactiver le mode vanish. |
| `/mreload`     | Recharger la configuration du plugin. |

---

## 📦 Installation

1. Téléchargez la dernière version compilée de **ModSuite**.
2. Placez le fichier `.jar` dans le dossier `plugins` de votre serveur Minecraft.
3. Redémarrez le serveur.
4. Configurez le fichier `config.yml` selon vos besoins.
5. (Facultatif) Configurez votre webhook Discord pour recevoir les alertes.

---

## 🛠️ Permissions

*(à documenter selon votre configuration)*  
Chaque commande dispose de sa permission dédiée afin de contrôler qui peut l’utiliser.

---

## 📌 Remarques

- Le projet est **en cours de développement** et de nouvelles fonctionnalités seront ajoutées prochainement.
- Pensez à sauvegarder vos fichiers de configuration avant chaque mise à jour.

---

## 📄 Licence

Licence à définir.  
© 2025 Twitmund – Tous droits réservés.
