package me.branchpanic.mods.stockpile.api.storage

interface MassStorageProvider<T> {
    val backingStorage: MassStorage<T>
}