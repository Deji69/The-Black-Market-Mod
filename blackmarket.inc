// MASTER INCLUDE

CONST
DEBUG_STR_SAVCOM1       = "SAVE FILE INCOMPATIBLE"
DEBUG_STR_2             = "SAVE FILE VERSION = "
DEBUG_STR_3             = "BLACK MARKET VERSION = "

// Transaction Pseudo-class (4 byte values)
CTransaction_iTime      = -1@
CTransaction_iCash      = -2@
CTransaction_iDrugQty   = -3@
CTransaction_Time       = 1
CTransaction_Cash       = 2
CTransaction_DrugQty    = 3
CTransaction_SIZE       = 16

// Async Pseudo-class (4 byte values)
CAsync_FUNCTION         = -1@
//CAsync_POINTER          = -2@
CAsync_VARS_BEGIN       = -3@   // 12 vars
CAsync_NUM_VARS         = 12
CAsync_SIZE             = 64

MIN_DRUG                = 0
WEED                    = 0
COKE                    = 1
HEROIN                  = 2
ECSTASY                 = 3
STEROID                 = 4
SHROOM                  = 5
MAX_DRUG                = 5
MAX_DRUG_QTY            = 60

DRUG_FLAG_WEED          = 0x1
DRUG_FLAG_COKE          = 0x2
DRUG_FLAG_HEROIN        = 0x4
DRUG_FLAG_ECSTASY       = 0x8
DRUG_FLAG_STEROID       = 0x10
DRUG_FLAG_SHROOM        = 0x20
PED_DRUG_ENTRY_SIZE     = 0x3

PRICE_WEED              = 50
PRICE_COKE              = 170
PRICE_HEROIN            = 200
PRICE_ECSTASY           = 120
PRICE_STEROID           = 80
PRICE_SHROOM            = 100
FPRICE_WEED             = 50.0
FPRICE_COKE             = 170.0
FPRICE_HEROIN           = 200.0
FPRICE_ECSTASY          = 120.0
FPRICE_STEROID          = 80.0
FPRICE_SHROOM           = 100.0

GlobalVarIndex          = 0@
this                    = 0@
TEMP_1v                 = 1@v
TEMP_1                  = 1@
TEMP_2                  = 2@
TEMP_2v                 = 2@v
TEMP_3                  = 3@
TEMP_4                  = 4@
TEMP_5                  = 5@
TEMP_6                  = 6@
TEMP_7                  = 7@
TEMP_8                  = 8@
TEMP_9                  = 9@
TEMP_10                 = 10@
VAR_START_NUM           = 11
VAR_1                   = 11@
VAR_2                   = 12@
VAR_3                   = 13@
VAR_4                   = 14@
VAR_5                   = 15@
VAR_6                   = 16@
VAR_7                   = 17@
VAR_8                   = 18@
VAR_9                   = 19@
VAR_10                  = 20@
VAR_11                  = 21@
VAR_12                  = 22@
VAR_13                  = 23@
VAR_14                  = 24@
SaveDataIndex           = 31@
TIMERA                  = 32@
TIMERB                  = 33@

SAVE_DATA_SIZE          = 256

SBOOLS_bDrugBuyTutDone  = 0
SBOOLS_bDrugSellTutDone = 1
SBOOLS_sAbuseTutDone    = 2

HELPDISP_BUY_DRUGS      = 1
HELPDISP_SELL_DRUGS     = 2
HELPDISP_ABUSE_DRUGS    = 3

// SUPER VARS!
SUPERVAR                = string
SVar                    = 0@
gMasterProcPointer      = SVar(SVar,4s)
gNumProcVars            = SVar(-1@,4s)
gCurrentProcData        = SVar(-2@,4s)
gCurrentProcPointer     = SVar(-3@,4s)
gStackPointer           = SVar(-4@,4s)
gSaveDataPointer        = SVar(-5@,4s)
gHasVisitedSaveMenu     = SVar(-6@,4s)
gDebugModeOn            = SVar(-7@,4s)
gDebugStartTime         = SVar(-8@,4s)
gDebugEndTime           = SVar(-9@,4s)
gNumCycles              = SVar(-10@,4s)
gStartVarPointer        = SVar(-11@,4s)
gStartSaveVarPointer    = SVar(-12@,4s)
gPedDrugInfo            = SVar(-13@,4s)
gNumPedDrugInfoEntries  = SVar(-14@,4s)
gDrugTakenID            = SVar(-15@,4s)
gDealGoingOn            = SVar(-16@,4s)
gSmokeStartTime         = SVar(-17@,4s)
gWeedLevel              = SVar(-18@,4s)
gWeedLevelInSystem      = SVar(-19@,4s)
gWeedEffectLevel        = SVar(-20@,4s)
gWeedTimeLastTook       = SVar(-21@,4s)
gNumAnimReferences      = SVar(-22@,4s)
gSmokeBlown             = SVar(-23@,4s)
gLastSmokeEmission      = SVar(-24@,4s)
gSmokeBlownX            = SVar(-25@,4s)
gSmokeBlownY            = SVar(-26@,4s)
gSmokeBlownZ            = SVar(-27@,4s)
gHasOverdosed           = SVar(-28@,4s)
gTotalDrugLevel         = SVar(-29@,4s)
gTimeOverdosed          = SVar(-30@,4s)
gDrugLevelInSystem      = SVar(-31@,4s)
NUM_VARS                = 64

SaveVar                 = 31@
gSaveDataBegin          = SaveVar(SaveVar,4s)
gSaveBools              = SaveVar(-1@,4s)
gSaveDrugQtyBegin       = SaveVar(-2@,4s)
gSaveNumWeed            = SaveVar(-2@,4s)
gSaveNumCoke            = SaveVar(-3@,4s)
gSaveNumHeroin          = SaveVar(-4@,4s)
gSaveNumEcstasy         = SaveVar(-5@,4s)
gSaveNumSteroid         = SaveVar(-6@,4s)
gSaveNumShroom          = SaveVar(-7@,4s)
gSaveNumDealsDone       = SaveVar(-8@,4s)
gSaveSafeDrugQtyBegin   = SaveVar(-9@,4s)
gSaveSafeNumWeed        = SaveVar(-9@,4s)
gSaveSafeNumCoke        = SaveVar(-10@,4s)
gSaveSafeNumHeroin      = SaveVar(-11@,4s)
gSaveSafeNumEcstasy     = SaveVar(-12@,4s)
gSaveSafeNumSteroid     = SaveVar(-13@,4s)
gSaveSafeNumShroom      = SaveVar(-14@,4s)
gSaveSupply             = SaveVar(-15@,4s)
gSaveSupplyWeed         = SaveVar(-15@,4s)
gSaveSupplyCoke         = SaveVar(-16@,4s)
gSaveSupplyHeroin       = SaveVar(-17@,4s)
gSaveSupplyEcstasy      = SaveVar(-18@,4s)
gSaveSupplySteroid      = SaveVar(-19@,4s)
gSaveSupplyShroom       = SaveVar(-20@,4s)
gSaveDemand             = SaveVar(-21@,4s)
gSaveDemandWeed         = SaveVar(-21@,4s)
gSaveDemandCoke         = SaveVar(-22@,4s)
gSaveDemandHeroin       = SaveVar(-23@,4s)
gSaveDemandEcstasy      = SaveVar(-24@,4s)
gSaveDemandSteroid      = SaveVar(-25@,4s)
gSaveDemandShroom       = SaveVar(-26@,4s)
gSaveStatsBegin         = SaveVar(-27@,4s)
gSaveDrugCashEarned     = SaveVar(-27@,4s)      // stat 800
gSaveDrugProfit         = SaveVar(-28@,4s)      // stat 801
gSaveDealerDebt         = SaveVar(-29@,4s)      // stat 802
//gSaveDrugLoss           = SaveVar(-30@,4s)      // stat 803
gSaveAbuseStatsBegin    = SaveVar(-31@,4s)
gSaveStatWeedTaken      = SaveVar(-31@,4s)      // stat 804
gSaveStatCokeTaken      = SaveVar(-32@,4s)      // stat 805
gSaveStatHeroinTaken    = SaveVar(-33@,4s)      // stat 806
gSaveStatEcstasyTaken   = SaveVar(-34@,4s)      // stat 807
gSaveStatSteroidsTaken  = SaveVar(-35@,4s)      // stat 808
gSaveStatShroomsTaken   = SaveVar(-36@,4s)      // stat 809
gSaveStatLevel          = SaveVar(-37@,4s)      // stat 810
gSaveStatEpisode        = SaveVar(-38@,4s)      // stat 811
gSaveFloatStatsBegin    = SaveVar(-52@,4s)
gSaveStatReputation     = SaveVar(-52@,4s)      // stat 825
gSaveStatExperience     = SaveVar(-53@,4s)      // stat 826
gSaveStatDrugExperience = SaveVar(-54@,4s)      // stat 827
gSaveStatVersion        = SaveVar(-77@,4s)      // stat 850 // (dummy)
gSaveStatsEnd           = SaveVar(-77@,4s)
gSaveNumHomiesDealt     = SaveVar(-78@,4s)
NUM_SAVE_VARS           = 128

// DEALER
DEALER_CHAR             = 0@
DEALER_STAGE            = 1@
DEALER_CONVO_STAGE      = 2@
DEALER_POSX             = 3@
DEALER_POSY             = 4@
DEALER_POSZ             = 5@
DEALER_BUYERPOSX        = 6@
DEALER_BUYERPOSY        = 7@
DEALER_BUYERPOSZ        = 8@
DEALER_DEALERDIST       = 9@
DEALER_PHONE_STAGE      = 10@
DEALER_BUYER            = 11@
DEALER_BUYERCAR         = 12@
DEALER_TEMPINT1         = 13@
DEALER_TIMELIMIT        = 14@
DEALER_CONVO_RESPONSE   = 15@
DEALER_TEMPINT2         = 16@
DEALER_ZONENAME         = 17@
DEALER_TASK_SEQUENCE    = 18@
DEALER_DEAL2PLAYER      = 19@
DEALER_CURRENTDRUG      = 20@
DEALER_THREATENED       = 21@
DEALER_MARKER           = 22@
DEALER_HASDRUGS         = 23@
DEALER_DRUGS            = 24@
DEALER_NUMWEED          = 24@
DEALER_NUMCOKE          = 25@
DEALER_NUMHEROIN        = 26@
DEALER_NUMECSTASY       = 27@
DEALER_NUMSTEROID       = 28@
DEALER_NUMSHROOM        = 29@
DEALER_MONEY            = 30@
DEALER_BMSETUP          = 31@
DEALER_TIMERA           = 32@
DEALER_TIMERB           = 33@

DEALER_VAR_CHAR         = 0
DEALER_VAR_BUYER        = 11
DEALER_VAR_BUYERCAR     = 12
DEALER_VAR_DEAL2PLAYER  = 19
DEALER_VAR_MARKER       = 22
DEALER_VAR_NUMWEED      = 24
DEALER_VAR_NUMCOKE      = 25
DEALER_VAR_NUMHEROIN    = 26
DEALER_VAR_NUMECSTASY   = 27
DEALER_VAR_NUMSTEROID   = 28
DEALER_VAR_NUMSHROOM    = 29
DEALER_VAR_MONEY        = 30
DEALER_VAR_BMPROCESSED  = 31

// Used AudioEvents
SOUND_MOBRING           = 23000

// Used StatID's
STAT_DRUGS_BUDGET       = 61
STAT_DRUGS_SOLD         = 217
STAT_DRUGS_BOUGHT       = 218

{// Reputation Increase Values (out of 1,000,000)
REP_PTS_DRUG_SOLD       = 10

// Reputation Decrease Values
REP_PTS_DEALER_KILLED   = 250
REP_PTS_DEALER_HARMED   = 50}
END
VAR
this:array 4 of string
END