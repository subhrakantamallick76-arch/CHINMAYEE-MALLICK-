package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val matchId: String,
    val teamName: String,
    val captainId: String,
    val viceCaptainId: String,
    val playerIdsJson: String, // Comma separated player IDs
    val totalPoints: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "joined_contests")
data class JoinedContestEntity(
    @PrimaryKey val id: String,
    val contestId: String,
    val matchId: String,
    val teamId: String,
    val teamName: String,
    val contestTitle: String,
    val entryFee: Int,
    val currentRank: Int,
    val totalParticipants: Int,
    val fantasyPoints: Int,
    val prizeWon: Int,
    val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_wallet")
data class WalletEntity(
    @PrimaryKey val id: Int = 1,
    val depositBalance: Double,
    val winningBalance: Double,
    val bonusBalance: Double
)

@Entity(tableName = "wallet_transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val isCredit: Boolean,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)
