package fr.cinpiros.exeption;

import org.bukkit.Bukkit;

public class TaskCreateException extends Throwable{
    public TaskCreateException(String message) {
        Bukkit.getLogger().warning(message);
    }

}
