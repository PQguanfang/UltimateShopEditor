package cn.superiormc.ultimateshop.gui.inv.editor;

import cn.superiormc.ultimateshop.UltimateShop;
import cn.superiormc.ultimateshop.gui.InvGUI;
import cn.superiormc.ultimateshop.gui.inv.GUIMode;
import cn.superiormc.ultimateshop.managers.LanguageManager;
import cn.superiormc.ultimateshop.methods.ReloadPlugin;
import cn.superiormc.ultimateshop.objects.ObjectShop;
import cn.superiormc.ultimateshop.utils.CommonUtil;
import cn.superiormc.ultimateshop.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EditShopGUI extends InvGUI {

    public YamlConfiguration config = null;

    private ObjectShop shop;

    public EditShopGUI(Player owner, ObjectShop shop) {
        super(owner);
        this.config = shop.getShopConfig();
        this.shop = shop;
    }

    @Override
    protected void constructGUI() {
        // shop name
        ItemStack shopNameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta tempVal1 = shopNameItem.getItemMeta();
        tempVal1.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "create-shop-gui.shop-name.name")));
        tempVal1.setLore(CommonUtil.modifyList(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                        "create-shop-gui.shop-name.lore")),
                "value",
                config.getString("settings.shop-name", "Unset, default to 'Shop'")));
        shopNameItem.setItemMeta(tempVal1);
        // buy more
        ItemStack buyMoreItem = new ItemStack(Material.BEACON);
        ItemMeta tempVal3 = shopNameItem.getItemMeta();
        tempVal3.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "create-shop-gui.buy-more.name")));
        tempVal3.setLore(CommonUtil.modifyList(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                "create-shop-gui.buy-more.lore")),
                "value",
                config.getString("settings.buy-more", "Unset, default to true")));
        buyMoreItem.setItemMeta(tempVal3);
        // menu
        ItemStack menuItem = new ItemStack(Material.CHEST);
        ItemMeta tempVal4 = shopNameItem.getItemMeta();
        tempVal4.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "create-shop-gui.menu-id.name")));
        tempVal4.setLore(CommonUtil.modifyList(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                        "create-shop-gui.menu-id.lore")),
                "value",
                config.getString("settings.menu", "Unset")));
        menuItem.setItemMeta(tempVal4);
        // send message
        ItemStack sendMessageAfterBuyItem = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta tempVal5 = shopNameItem.getItemMeta();
        tempVal5.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "create-shop-gui.send-message-after-buy.name").replace("{shop}", config.getName())));
        tempVal5.setLore(CommonUtil.modifyList(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                        "create-shop-gui.send-message-after-buy.lore")),
                "value",
                config.getString("settings.send-message-after-buy", "Unset, default to true")));
        sendMessageAfterBuyItem.setItemMeta(tempVal5);
        // product
        ItemStack createProductItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta tempVal2 = createProductItem.getItemMeta();
        tempVal2.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "edit-shop-gui.create-product.name")));
        tempVal2.setLore(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                "edit-shop-gui.create-product.lore")));
        createProductItem.setItemMeta(tempVal2);
        // edit product
        ItemStack editProductItem = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta tempVal7 = editProductItem.getItemMeta();
        tempVal7.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "edit-shop-gui.edit-product.name")));
        tempVal7.setLore(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                "edit-shop-gui.edit-product.lore")));
        editProductItem.setItemMeta(tempVal7);
        // finish
        ItemStack finishItem = new ItemStack(Material.GREEN_DYE);
        ItemMeta tempVal6 = finishItem.getItemMeta();
        tempVal6.setDisplayName(TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                "create-shop-gui.finish.name")));
        tempVal6.setLore(TextUtil.getListWithColor(LanguageManager.languageManager.getStringListText("editor." +
                "create-shop-gui.finish.lore")));
        finishItem.setItemMeta(tempVal6);
        if (Objects.isNull(inv)) {
            inv = Bukkit.createInventory(owner, 9,
                    TextUtil.parse(LanguageManager.languageManager.getStringText("editor." +
                            "edit-shop-gui.title")));
        }
        inv.setItem(0, shopNameItem);
        inv.setItem(1, buyMoreItem);
        inv.setItem(2, menuItem);
        inv.setItem(3, sendMessageAfterBuyItem);
        inv.setItem(4, createProductItem);
        inv.setItem(5, editProductItem);
        inv.setItem(8, finishItem);
    }

    @Override
    public boolean clickEventHandle(Inventory inventory, ClickType type, int slot) {
        if (slot == 0) {
            guiCache.put(owner, this);
            guiMode = GUIMode.EDIT_SHOP_NAME;
            LanguageManager.languageManager.sendStringText(owner, "editor.enter-shop-name");
            owner.closeInventory();
        }
        if (slot == 1) {
            if (config.getString("settings.buy-more") != null &&
                    config.getString("settings.buy-more").equals("true")) {
                config.set("settings.buy-more", "false");
            } else {
                config.set("settings.buy-more", "true");
            }
            constructGUI();
        }
        if (slot == 2) {
            guiCache.put(owner, this);
            guiMode = GUIMode.EDIT_MENU_ID;
            LanguageManager.languageManager.sendStringText(owner, "editor.enter-menu-id");
            owner.closeInventory();
        }
        if (slot == 3) {
            if (config.getString("settings.send-message-after-buy") != null &&
                    config.getString("settings.send-message-after-buy").equals("true")) {
                config.set("settings.send-message-after-buy", "false");
            } else {
                config.set("settings.send-message-after-buy", "true");
            }
            constructGUI();
        }
        if (slot == 5) {
            guiMode = GUIMode.OPEN_NEW_GUI;
            ChooseProductGUI gui = new ChooseProductGUI(owner, this);
            gui.openGUI();
        }
        if (slot == 8) {
            if (UltimateShop.freeVersion) {
                owner.closeInventory();
                owner.sendMessage("§x§9§8§F§B§9§8[UltimateShop] §cError: You are now using free version, " +
                        "your changes in GUI Editor won't get saved.");
                return true;
            }
            File dir = new File(UltimateShop.instance.getDataFolder() + "/shops");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, shop.getShopName() + ".yml");
            file.delete();
            try {
                config.save(file);
                ReloadPlugin.reload(owner);
                LanguageManager.languageManager.sendStringText(owner,
                        "editor.shop-edited",
                        "shop",
                        config.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            owner.closeInventory();
        }
        return true;
    }

    public ObjectShop getShop() {
        return shop;
    }
}
