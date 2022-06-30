// ================================ Black Market Mod Source, by Deji (http://gtag.gtagaming.com) ================================
{$CLEO}
{$VERSION 3.0.1000}
{$I constants.inc}
{$I blackmarket.inc}

CONST
VERSION_LONG            = 0x01010001    // byte(major), byte(episode), byte(minor), byte(beta)
VERSION_STRING          = "ver._1.0.1"  // for menu stat
_DEBUG                  = 0
END
VAR
    SVar:array 4 of SUPERVAR
    CSprite:array 4 of SUPERVAR
END

WAIT 0
WAIT 0
SCRIPT_NAME "blackma"

IF NOT IS_GAME_VERSION_ORIGINAL
THEN
    PRINTSTRING "GAME_VERSION_NOT_ORIGINAL"
    TERMINATE_THIS_CUSTOM_SCRIPT
END
IF NOT IS_BIT_SET_L 38@ 9
THEN
    PRINTSTRING "SUPERVARS_NOT_INSTALLED"
    TERMINATE_THIS_CUSTOM_SCRIPT
END

CALL @ClearLog 0
GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Initiating"
CALL @Log 0

{********************************************}

// Initiate SuperVars
SET_BIT_L 38@ 8
CALL @calloc 2 4 NUM_VARS GlobalVarIndex
CALL @calloc 2 4 NUM_SAVE_VARS SaveVar
TEMP_1 = @GLOBAL_VARIABLES_INDEX
0085: TEMP_1(TEMP_1,4s) = GlobalVarIndex
0085: gSaveDataPointer = SaveVar

GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Loading data"
CALL @Log 0

{********************************************}

IF OPEN_FILE "CLEO\BlackMarket\drug_peds.dat" "rb" TEMP_1
THEN
    READ_FROM_FILE TEMP_1 4 TEMP_2
    CALL @malloc 1 TEMP_2 TEMP_3
    READ_FROM_FILE TEMP_1 TEMP_2 TEMP_3(TEMP_3,4s)
    TEMP_2 /= PED_DRUG_ENTRY_SIZE
    TEMP_2 += PED_DRUG_ENTRY_SIZE
    0085: gNumPedDrugInfoEntries = TEMP_2
    0085: gPedDrugInfo = TEMP_3
    CLOSE_FILE TEMP_1
ELSE
    PRINTSTRING "DAT_READ_ERROR"
    GET_LABEL_POINTER @TempMemory TEMP_1
    STRING_FORMAT TEMP_1 "Error loading data file 'drug_peds.dat'"
    CALL @Error 1 TEMP_1
    TERMINATE_THIS_SCRIPT
END

{********************************************}

GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Loading anims"
CALL @Log 0

// Load our anims:
GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "CLEO\BlackMarket\BlackMarket.ifp"
GET_LABEL_POINTER @MoreMemory TEMP_2
06D2: TEMP_2(TEMP_2,1s) = "Blackmarket"
CALL_FUNCTION_RETURN 0x4D3940 1 1 TEMP_2 TEMP_3
IF TEMP_3 == 0
THEN
    CALL_FUNCTION 0x4D55D0 1 1 TEMP_1
    CALL_FUNCTION_RETURN 0x4D3940 1 1 TEMP_2 TEMP_3
    IF TEMP_3 == 0
    THEN
        PRINTSTRING "IFP_READ_ERROR"
        GET_LABEL_POINTER @TempMemory TEMP_1
        STRING_FORMAT TEMP_1 "Error loading anim file 'BlackMarket.ifp' (0x%X)" TEMP_3
        CALL @Error 1 TEMP_1
        TERMINATE_THIS_SCRIPT
    END
END

{********************************************}

// Load our textures:
{GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "blackmarket"
CALL_FUNCTION_RETURN 0x731850 1 1 TEMP_1 TEMP_2     // FindSlot
IF TEMP_2 == -1
THEN
    CALL_FUNCTION_RETURN 0x731C80 1 1 TEMP_1 TEMP_2     // AddSlot
END
STRING_FORMAT TEMP_1 "cleo\BlackMarket\bmm.txd"
CALL_FUNCTION 0x7320B0 2 2 TEMP_1 TEMP_2    // LoadTxd
CALL_FUNCTION 0x731A00 1 1 TEMP_2           // AddRef
CALL_FUNCTION 0x7316A0 0 0                  // Push
CALL_FUNCTION 0x7319C0 1 1 TEMP_2           // SetCurrent
GET_LABEL_POINTER @Textures TEMP_2
06D2: TEMP_1(TEMP_1,4s) = "drug1"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "drug2"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "drug3"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "drug4"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "drug5"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "drug6"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug1"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug2"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug3"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug4"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug5"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "nodrug6"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
TEMP_2 += 4
06D2: TEMP_1(TEMP_1,4s) = "redcirc"
CALL_METHOD 0x727270 TEMP_2 1 0 TEMP_1
CALL_FUNCTION 0x7316B0 0 0                  // Pop}

LOAD_TEXTURE_DICTIONARY "BMM"
LOAD_SPRITE 1 "drug1"
LOAD_SPRITE 2 "drug2"
LOAD_SPRITE 3 "drug3"
LOAD_SPRITE 4 "drug4"
LOAD_SPRITE 5 "drug5"
LOAD_SPRITE 6 "drug6"
LOAD_SPRITE 7 "nodrug1"
LOAD_SPRITE 8 "nodrug2"
LOAD_SPRITE 9 "nodrug3"
LOAD_SPRITE 10 "nodrug4"
LOAD_SPRITE 11 "nodrug5"
LOAD_SPRITE 12 "nodrug6"
LOAD_SPRITE 13 "redcirc"

{********************************************}

GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Hooking stats"
CALL @Log 0

// Inject our own stat page:
0A90: TEMP_2 = 0x12 * 8
0A8E: TEMP_1 = 0x8CE012 + TEMP_2
WRITE_MEMORY TEMP_1 1 0x15 FALSE
TEMP_1 += 1
05A9: TEMP_1(TEMP_1,8s) = '_BMMNM'
TEMP_1 += 8
0006: TEMP_1(TEMP_1,4s) = 0x3900000C
0006: TEMP_1(-1@,4s) = 0x01014A00
TEMP_1 += 7
WRITE_MEMORY TEMP_1 2 0x1 FALSE

// Not to mess up the "Back" button, though
0A90: TEMP_2 = 0x12 * 9
0A8E: TEMP_1 = 0x8CE012 + TEMP_2
WRITE_MEMORY TEMP_1 1 0x2 FALSE
TEMP_1 += 1
05A9: TEMP_1(TEMP_1,8s) = 'FEDS_TB'
TEMP_1 += 8
0006: TEMP_1(TEMP_1,4s) = 0x40002A0B
0006: TEMP_1(-1@,4s) = 0x03017C01
TEMP_1 += 7
WRITE_MEMORY TEMP_1 2 0x3 FALSE

// Move entries up to make space for ours
WRITE_MEMORY 0x8CE020 2 0x5A FALSE

// Make it functional by increasing amount of stat struct arrays
WRITE_MEMORY 0x55A78F 1 8 TRUE
GET_LABEL_POINTER @_STATSWITCH TEMP_1
WRITE_MEMORY 0x55A796 4 TEMP_1 TRUE
TEMP_1 += 32
GET_LABEL_POINTER @_STATGETASM TEMP_2
0085: TEMP_1(TEMP_1,4s) = TEMP_2
TEMP_2 += 1
GET_LABEL_POINTER @_STATDATA TEMP_2(TEMP_2,4s)

// Get our own range of stats
GET_LABEL_POINTER @_STATGXT TEMP_1
0A8E: TEMP_2 = TEMP_1 + 0x15
0A8F: TEMP_3 = TEMP_1 - 8
WRITE_MEMORY TEMP_2 4 TEMP_3 FALSE
TEMP_1 -= 0x55A83E
WRITE_MEMORY 0x55A839 1 0xE9 TRUE
WRITE_MEMORY 0x55A83A 4 TEMP_1 TRUE

GET_LABEL_POINTER @_STATDISP TEMP_1
0A8E: TEMP_2 = TEMP_1 + 0x15
WRITE_MEMORY TEMP_2 4 TEMP_3 FALSE
TEMP_1 -= 0x58BC25
WRITE_MEMORY 0x58BC20 1 0xE9 TRUE
WRITE_MEMORY 0x58BC21 4 TEMP_1 TRUE

GET_LABEL_POINTER @_STATARRAY TEMP_1
0A8E: TEMP_2 = TEMP_1 + 0x20
GET_VAR_POINTER gSaveStatsBegin TEMP_2(TEMP_2,4s)
TEMP_1 -= 0x55A807
WRITE_MEMORY 0x55A802 1 0xE9 TRUE
WRITE_MEMORY 0x55A803 4 TEMP_1 TRUE

GET_LABEL_POINTER @_STATGETINTASM TEMP_1
0A8E: TEMP_2 = TEMP_1 + 0x20
GET_VAR_POINTER gSaveStatsBegin TEMP_2(TEMP_2,4s)
TEMP_1 -= 0x55AAB7
WRITE_MEMORY 0x55AAB2 1 0xE9 TRUE
WRITE_MEMORY 0x55AAB3 4 TEMP_1 TRUE

// Lets have some special stat slots :)
GET_LABEL_POINTER @_SPECIALSTAT TEMP_1
0A8F: TEMP_2 = TEMP_1 - 0x55AE5F
WRITE_MEMORY 0x55AE5B 4 TEMP_2 TRUE
0A8E: TEMP_2 = TEMP_1 + 27
GET_LABEL_POINTER @_SPECIALSTAT_TT TEMP_2(TEMP_2,4s)
0A8E: TEMP_2 = TEMP_1 + 34
GET_LABEL_POINTER @_SPECIALSTAT_JT TEMP_1
0085: TEMP_2(TEMP_2,4s) = TEMP_1

FOR TEMP_2 = 0 TO 1
    GET_LABEL_POINTER TEMP_1(TEMP_2,4s) TEMP_1(TEMP_2,4s)
END

// Set up GetPlayerStat
GET_LABEL_POINTER @_GETSTAT TEMP_1
0A8E: TEMP_2 = TEMP_1 + 0x12
GET_VAR_POINTER gSaveStatsBegin TEMP_2(TEMP_2,4s)
0A8E: TEMP_2 = TEMP_1 + 0x1A
GET_VAR_POINTER gSaveStatsBegin TEMP_2(TEMP_2,4s)

// Patch GetPlayerStat
TEMP_1 -= 0x558E61
WRITE_MEMORY 0x558E56 4 0x03203D66 TRUE
WRITE_MEMORY 0x558E5A 2 0x057C TRUE
WRITE_MEMORY 0x558E5C 1 0xE9 TRUE
WRITE_MEMORY 0x558E5D 4 TEMP_1 TRUE
WRITE_MEMORY 0x558E61 4 0xDBC8B70F TRUE
WRITE_MEMORY 0x558E65 4 0x8E208D04 TRUE
WRITE_MEMORY 0x558E69 4 0x90C300B7 TRUE

{********************************************}

// Patch FIND_NEAREST_CHAR_WITH_BUY_DRUGS_ATTRIBUTE to allow gang members to buy
WRITE_MEMORY 0x474ABE 4 0xFEB TRUE

{********************************************}

{GET_LABEL_POINTER @_DrawSprite_ASM TEMP_1
WRITE_MEMORY 0x464A7A 1 0xE9 TRUE
0A8F: TEMP_2 = TEMP_1 - 0x464A7F
WRITE_MEMORY 0x464A7B 4 TEMP_2 TRUE
WRITE_MEMORY 0x464B6D 1 0xE9 TRUE
0A8F: TEMP_2 = TEMP_1 - 0x464B72
WRITE_MEMORY 0x464B6E 4 TEMP_2 TRUE}

{********************************************}

GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Adding text labels"
CALL @Log 0

ADD_TEXT_LABEL "_BMMNM"  "Black Market"
ADD_TEXT_LABEL "_BMMDB"  "Made by Deji"
ADD_TEXT_LABEL "_BMWWW"  "www.gtag.gtagaming.com/mods/99-the-black-market-mod/"
ADD_TEXT_LABEL "_FLOAT"  "~1~.~1~"
ADD_TEXT_LABEL "_BMDQ0"  "~1~x Weed"
ADD_TEXT_LABEL "_BMDQ1"  "~1~x Coke"
ADD_TEXT_LABEL "_BMDQ2"  "~1~x Heroin"
ADD_TEXT_LABEL "_BMDQ3"  "~1~x Ecstasy"
ADD_TEXT_LABEL "_BMDQ4"  "~1~x Steroids"
ADD_TEXT_LABEL "_BMDQ5"  "~1~x Shrooms"
ADD_TEXT_LABEL "_BMTOT"  "Total: $~1~"
ADD_TEXT_LABEL "_BMD1"   "You want some stuff?"
ADD_TEXT_LABEL "_BMD1Y"  "OK then."
ADD_TEXT_LABEL "_BMD1N"  "Nah, get outta here."
ADD_TEXT_LABEL "_BMD2"   "Take your pick."
ADD_TEXT_LABEL "_BMD3"   "Maybe another time?"
ADD_TEXT_LABEL "_BMDRG"  "Drugs"
ADD_TEXT_LABEL "_BMSTA"  "Stats"
ADD_TEXT_LABEL "_BMSHD"  "Stash"
ADD_TEXT_LABEL "_BMPKT"  "Pocket"
ADD_TEXT_LABEL "_BMCAR"  "Drug Peddling"
ADD_TEXT_LABEL "_BMS800" "Money made from drugs"
ADD_TEXT_LABEL "_BMS801" "Drugs profit"
ADD_TEXT_LABEL "_BMS802" "Money owed to dealer"
ADD_TEXT_LABEL "_BMS803" "Drugs loss"
ADD_TEXT_LABEL "_BMS804" "Times Weed Abused"
ADD_TEXT_LABEL "_BMS805" "Times Coke Abused"
ADD_TEXT_LABEL "_BMS806" "Times Heroin Abused"
ADD_TEXT_LABEL "_BMS807" "Times Ecstasy Abused"
ADD_TEXT_LABEL "_BMS808" "Times Steroids Abused"
ADD_TEXT_LABEL "_BMS809" "Times Shrooms Abused"
ADD_TEXT_LABEL "_BMS810" "Level"
ADD_TEXT_LABEL "_BMS825" "Reputation"
ADD_TEXT_LABEL "_BMS826" "Experience"
ADD_TEXT_LABEL "_BMS827" "Drug Experience"
ADD_TEXT_LABEL "_BMS850" VERSION_STRING

{********************************************}

GET_LABEL_POINTER @TempMemory TEMP_1
STRING_FORMAT TEMP_1 "Finale"
CALL @Log 0

CALL @Stats_Init 0

0006: gSaveBools = 0

// Determine whether we're on a loaded game.
GET_INT_STAT 136 TEMP_1
IF TEMP_1 > 0
THEN
    // Get the load slot used and load the Black Market Save file.
    READ_MEMORY 0xBA68A7 1 0 TEMP_1
    CALL @System_Load 2 GlobalVarIndex TEMP_1
END

// Finale Setups
GET_THIS_SCRIPT_STRUCT gMasterProcPointer
0085: gCurrentProcPointer = gMasterProcPointer

0006: gDebugModeOn = _DEBUG
0006: gDrugTakenID = -1

// Generate prices now (values are 'specific' to buying peds)
CALL @GenerateDrugPrices 0

// Get rid of 05B6 in the memory spaces
GET_LABEL_POINTER @TempMemory TEMP_1
WRITE_MEMORY TEMP_1 2 0 FALSE
GET_LABEL_POINTER @MoreMemory TEMP_1
WRITE_MEMORY TEMP_1 2 0 FALSE
GET_LABEL_POINTER @EvenMoreMemory TEMP_1
WRITE_MEMORY TEMP_1 2 0 FALSE
GET_LABEL_POINTER @STACK TEMP_1
FOR TEMP_2 = 0 TO 4
    0006: TEMP_1(TEMP_1,4s) = 0
    TEMP_1 += 128
END

{********************************************}

// PROCESS THE CALL LIST

WHILE TRUE
    003D:
    
    // Initialise the stack
    GET_LABEL_POINTER @STACK gStackPointer
    
    TEMP_1 = @CALL_LIST
    TEMP_2 = 0
    
    WHILE 8039:   NOT TEMP_1(TEMP_2,4s) == 0
        IF 0039:   TEMP_1(TEMP_2,4s) == @Debug
        THEN
            TEMP_3 = _DEBUG
            IF NOT TEMP_3 == 1
            THEN
                TEMP_2 += 1
                CONTINUE
            END
        END
        
        0085: TEMP_3 = TEMP_1(TEMP_2,4s)
        
        GET_LABEL_POINTER TEMP_3 gCurrentProcData
        
        // Get number of required proc vars, then repair the func
        READ_MEMORY gCurrentProcData 2 0 gNumProcVars
        GET_VAR_POINTER TEMP_3(TEMP_3,2s) TEMP_3
        WRITE_MEMORY TEMP_3 2 0x50 0
        
        USE_TEXT_COMMANDS FALSE
        
        // Call it!
        CALL TEMP_1(TEMP_2,4s) 1 GlobalVarIndex
        TEMP_2 += 1
    END
    
    // For trivial purposes...
    000A: gNumCycles += 1
END

// List of funcs to call in-order (act like new scripts but can backreference the master)

:CALL_LIST
HEX
@Debug
@System
@GUI
@Abuse
@Submissions
@Async_Sys
//@Market           // TODO: Make market system more advanced & location-specific?
@DealerHook
@FIENDS
@End
END
0000:
0000:

{********************************************}

// Debugging time-savers

:Debug
HEX
0200 01 @InitProc
END

// Roll-on SCRambl '#ifdef'!

{STORE_DEBUG_COMMAND TEMP_1v
IF COMPARE_STRING_LS16 TEMP_1v "BMMDEBUG"
THEN
    CLEAR_DEBUG_COMMAND
    IF 0039:   gDebugModeOn == 0
    THEN
        0006: gDebugModeOn = 1
        PRINTSTRING "DEBUG_MODE_ENABLED"
    ELSE
        0006: gDebugModeOn = 0
        PRINTSTRING "DEBUG_MODE_DISABLED"
    END
ELSE
    IF 0039:   gDebugModeOn == 0
    THEN
        RET 0
    END
END

IF COMPARE_STRING_LS16 TEMP_1v "BMMQUICK"
THEN
    START_NEW_CUSTOM_SCRIPT "blackmarket.cs"
    PRINTSTRING "BLACK_MARKET_LAZY_RESTART"
    TERMINATE_THIS_CUSTOM_SCRIPT
END

IF COMPARE_STRING_LS16 TEMP_1v "STATUSQUO"
THEN
    CLEAR_DEBUG_COMMAND
    PRINTFLOAT2 "REP" gSaveStatReputation
END

IF COMPARE_STRING_LS16 TEMP_1v "JUSTALILRESPECT"
THEN
    CLEAR_DEBUG_COMMAND
    CALL @Stat_IncRep 1 50000000
END

IF COMPARE_STRING_LS16 TEMP_1v "THEYBEDISSIN"
THEN
    CLEAR_DEBUG_COMMAND
    CALL @Stat_DecRep 1 50000000
END

IF COMPARE_STRING_LS16 TEMP_1v "ANIM"
THEN
    CLEAR_DEBUG_COMMAND
    REQUEST_ANIMATION "BLACKMARKET"
    WHILE NOT HAS_ANIMATION_LOADED "BLACKMARKET"
        WAIT 0
    END
    TASK_PLAY_ANIM_UPPER_BODY scplayer "dazed_2" "BLACKMARKET" 4.0 0 0 0 0 -1
    IF 8019:   NOT gNumAnimReferences > 0
    THEN
        REMOVE_ANIMATION "BLACKMARKET"
    END
END

// Get some drugs on tick.
IF COMPARE_STRING_LS16 TEMP_1v "TICKME"
THEN
    CLEAR_DEBUG_COMMAND
    GET_VAR_POINTER gSaveDrugQtyBegin TEMP_2
    FOR TEMP_3 = MIN_DRUG TO MAX_DRUG
        000A: TEMP_2(TEMP_3,4s) += 1
    END
END

// Fill pockets.
IF COMPARE_STRING_LS16 TEMP_1v "FULLOFSTONES"
THEN
    CLEAR_DEBUG_COMMAND
    GET_VAR_POINTER gSaveDrugQtyBegin TEMP_2
    FOR TEMP_3 = MIN_DRUG TO MAX_DRUG
        0006: TEMP_2(TEMP_3,4s) = 20
    END
END

// Stroke the furry wall!
IF COMPARE_STRING_LS16 TEMP_1v "HANDMEAJEFFREY"
THEN
    CLEAR_DEBUG_COMMAND
    GET_VAR_POINTER gSaveDrugQtyBegin TEMP_2
    FOR TEMP_3 = MIN_DRUG TO MAX_DRUG
        GENERATE_RANDOM_INT_IN_RANGE -10 10 TEMP_4
        IF TEMP_4 < 0
        THEN
            TEMP_4 = 0
        END 
        0085: TEMP_2(TEMP_3,4s) = TEMP_4
    END
END

// +1 weed level
IF COMPARE_STRING_LS16 TEMP_1v "PUFF"
THEN
    CLEAR_DEBUG_COMMAND
    CALL @IncDrugLevel 2 WEED 1
END

// Sober up
IF COMPARE_STRING_LS16 TEMP_1v "HAIROFADOG"
THEN
    CLEAR_DEBUG_COMMAND
    0006: gWeedLevel = 0
END

IF IS_KEYBOARD_KEY_JUST_PRESSED 0x4C
THEN
    PRINTNL
    PRINTINT2 "LVL_WEED" gWeedLevelInSystem
END

// Show the drug qty's
IF IS_KEYBOARD_KEY_JUST_PRESSED 0x51
THEN
    PRINTNL
    PRINTINT2 "QTY_SHROOM" gSaveNumShroom
    PRINTINT2 "QTY_STEROID" gSaveNumSteroid
    PRINTINT2 "QTY_ECSTASY" gSaveNumEcstasy
    PRINTINT2 "QTY_HEROIN" gSaveNumHeroin
    PRINTINT2 "QTY_COKE" gSaveNumCoke
    PRINTINT2 "QTY_WEED" gSaveNumWeed
END

// Show the market prices
IF IS_KEYBOARD_KEY_JUST_PRESSED 0x50
THEN
    PRINTNL
    CALL @GetDrugPrice 1 SHROOM TEMP_1
    PRINTINT2 "PRICE_SHROOM" TEMP_1
    CALL @GetDrugPrice 1 STEROID TEMP_1
    PRINTINT2 "PRICE_STEROID" TEMP_1
    CALL @GetDrugPrice 1 ECSTASY TEMP_1
    PRINTINT2 "PRICE_ECSTASY" TEMP_1
    CALL @GetDrugPrice 1 HEROIN TEMP_1
    PRINTINT2 "PRICE_HEROIN" TEMP_1
    CALL @GetDrugPrice 1 COKE TEMP_1
    PRINTINT2 "PRICE_COKE" TEMP_1
    CALL @GetDrugPrice 1 WEED TEMP_1
    PRINTINT2 "PRICE_WEED" TEMP_1
END

IF IS_KEYBOARD_KEY_JUST_PRESSED 0x56
THEN
    PRINTNL
    CALL @GetDrugValue 1 SHROOM TEMP_1
    PRINTINT2 "VALUE_SHROOM" TEMP_1
    CALL @GetDrugValue 1 STEROID TEMP_1
    PRINTINT2 "VALUE_STEROID" TEMP_1
    CALL @GetDrugValue 1 ECSTASY TEMP_1
    PRINTINT2 "VALUE_ECSTASY" TEMP_1
    CALL @GetDrugValue 1 HEROIN TEMP_1
    PRINTINT2 "VALUE_HEROIN" TEMP_1
    CALL @GetDrugValue 1 COKE TEMP_1
    PRINTINT2 "VALUE_COKE" TEMP_1
    CALL @GetDrugValue 1 WEED TEMP_1
    PRINTINT2 "VALUE_WEED" TEMP_1
END

// Display script speed
IF VAR_1 == 1
THEN
    IF 32@ > 500
    THEN
        0085: VAR_2 = gDebugEndTime
        0062: VAR_2 -= gDebugStartTime
        0093: VAR_2 = integer VAR_2 to_float
        VAR_2 /= 1000.0
        32@ = 0
    END
    SET_TEXT_COLOUR 0 180 0 255
    SET_TEXT_EDGE 1 RGBA 0 0 0 255
    DISPLAY_TEXT_WITH_FLOAT 5.0 5.0 "_FLOAT" VAR_2 2
ELSE
    VAR_1 = 1
END

READ_MEMORY 0xB610E0 4 0 gDebugStartTime

// Find all the dealers
GET_NUMBER_OF_SCRIPT_INSTANCES_OF_STREAMED_SCRIPT SCRIPT_DEALER STORE_TO TEMP_1

IF TEMP_1 > 0
THEN
    GET_LABEL_POINTER @TempMemory TEMP_2
    05A9: TEMP_2(TEMP_2,8s) = "bmdeal"
    
    IF CALL @CTheScripts__GetNextActiveScriptNamed 2 1 TEMP_2 >> TEMP_1
    THEN
        WHILE TRUE
            CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_CHAR TEMP_3
            CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_MARKER TEMP_4
            
            IF TEMP_3 <= 0x8C00
            THEN
                IF NOT IS_CHAR_DEAD TEMP_3
                THEN
                    IF NOT DOES_BLIP_EXIST TEMP_4
                    THEN
                        // Add a blip for easy finding
                        ADD_BLIP_FOR_CHAR TEMP_3 STORE_TO TEMP_4
                        CHANGE_BLIP_COLOUR TEMP_4 0xFFFFFFFF
                        CHANGE_BLIP_SCALE TEMP_4 2
                        CALL @CRunningScript__SetVar 3 TEMP_1 DEALER_VAR_MARKER TO TEMP_4
                    END
                    IF IS_PLAYER_TARGETTING_CHAR 0 TEMP_3
                    THEN
                        // Display some semi-useful infos
                        GET_LABEL_POINTER @TempMemory TEMP_4
                        SET_TEXT_COLOUR 255 255 255 255
                        SET_TEXT_EDGE 1 0 0 0 255
                        STRING_FORMAT TEMP_4 "DEALER (0x%X)" TEMP_1
                        CALL @DisplayText 3 5.0 430.0 TEMP_4
                        
                        SET_TEXT_COLOUR 0 255 0 255
                        SET_TEXT_EDGE 1 0 0 0 255
                        SET_TEXT_SIZE 640.0
                        CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_MONEY >> TEMP_3
                        STRING_FORMAT TEMP_4 "Cash: $%d" TEMP_3
                        CALL @DisplayText 3 170.0 430.0 TEMP_4
                        
                        SET_TEXT_COLOUR 255 255 255 255
                        SET_TEXT_EDGE 1 0 0 0 255
                        SET_TEXT_SIZE 640.0
                        CALL @GetDealerScriptTotalDrugQuantity 1 TEMP_1 TEMP_3
                        STRING_FORMAT TEMP_4 "Drugs: %d" TEMP_3
                        CALL @DisplayText 3 280.0 430.0 TEMP_4
                    END
                ELSE
                    IF DOES_BLIP_EXIST TEMP_4
                    THEN REMOVE_BLIP TEMP_4
                    END
                END
            END
            
            CALL @CTheScripts__GetNextActiveScriptNamed 2 0 TEMP_2 >> TEMP_1
            ELSE_GOTO BREAK
        END
    END
END}

GOSUB @EndProc
RET 0

{********************************************}

:System
hex
0600 01 @InitProc
end

// Black Market System. Non-conditional features.

GET_INTERIOR_FROM_CHAR scplayer TEMP_1
IF NOT TEMP_1 == 0
THEN
    GET_CHAR_INTERIOR_NAME scplayer TEMP_2v
    
    IF NOT IS_STRING_NULL_LV TEMP_2v
    THEN
        BITWISE_AND TEMP_2 0xFFFF TEMP_1
        IF OR
            COMPARE_STRING_LS16 TEMP_2v "CARLS"
            TEMP_1 == 0x5653
        THEN
            GOSUB @System_SafeHouse
        END
    END
END

IF VAR_1 == 1
THEN
    READ_MEMORY 0xBA68A6 1 0 TEMP_1
    IF OR
        TEMP_1 == 18  // Save Complete Screen
        TEMP_1 == 19  // Save Complete Screen 2
    THEN
        // So you wanna save the game?
        READ_MEMORY 0xBA68A7 1 0 TEMP_1
        CALL @System_Save 2 GlobalVarIndex TEMP_1
    END
    VAR_1 = 0
END

READ_MEMORY 0xBA67A7 1 0 TEMP_1

IF TEMP_1 == 1
THEN VAR_1 = 1
END

// Each episode has its limits...
IF 0039:   gSaveStatEpisode == 1
THEN
    IF 0031:   gSaveStatReputation >= 150.0
    THEN
        0007: gSaveStatReputation = 150.0
    END
    IF 0031:   gSaveStatDrugExperience >= 100.0
    THEN
        0007: gSaveStatDrugExperience = 100.0
    END
END

GOSUB @EndProc
RET 0

:System_SafeHouse

// Stash and retrieve drugs
IF AND
    IS_PLAYER_PLAYING player1
    IS_PLAYER_CONTROL_ON player1
THEN
    IF VAR_2 == 0
    THEN
        IF AND
            NOT IS_HELP_MESSAGE_BEING_DISPLAYED
            NOT GET_FADING_STATUS
        THEN
            CALL @GetTotalSafeDrugQuantity 0 TEMP_1
            CALL @GetTotalDrugQuantity 0 TEMP_2
            IF TEMP_1 > 0
            THEN
                PRINT_HELP_STRING "Hold ~k~~PED_FIREWEAPON_ALT~ to access your drug stash."
            ELSE
                IF TEMP_2 > 0
                THEN
                    PRINT_HELP_STRING "This safehouse can be used to stash drugs (hold ~k~~PED_FIREWEAPON_ALT~)."
                END
            END
            VAR_2 = 1
        END
    END
END
IF NOT VAR_3 == 2
THEN
    IF VAR_3 == 3
    THEN
        IF NOT IS_BUTTON_PRESSED PAD1 VAR_6
        THEN
            VAR_3 = 0
            VAR_6 = 0
            SET_PLAYER_CONTROL player1 ON
        END
    ELSE
        IF AND
            IS_PLAYER_PLAYING player1
            IS_PLAYER_CONTROL_ON player1
        THEN
            IF $Phone_Ringing_Flag == 0
            THEN
                IF AND
                    IS_BUTTON_PRESSED PAD1 LEFTSHOULDER1
                    NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1
                THEN
                    IF NOT VAR_3 == 0
                    THEN
                        GET_GAME_TIMER TEMP_1
                        0062: TEMP_1 -= VAR_4
                        IF TEMP_1 > 2000
                        THEN
                            SET_PLAYER_CONTROL player1 OFF
                            DISPLAY_RADAR FALSE
                            VAR_3 = 2
                        END
                    ELSE
                        VAR_3 = 1
                        GET_GAME_TIMER VAR_4
                    END
                ELSE
                    VAR_3 = 0
                END
            ELSE
                VAR_3 = 0
            END
        END
    END
ELSE
    IF IS_PLAYER_PLAYING player1
    THEN
        GET_PAD_STATE 0 LEFTSTICKX STORE_TO TEMP_1
        GET_PAD_STATE 0 LEFTSTICKY STORE_TO TEMP_2
        
        IF IS_PC_USING_JOYPAD
        THEN
            IF IS_BUTTON_PRESSED PAD1 DPADLEFT
            THEN
                TEMP_1 = -128
            END
            IF IS_BUTTON_PRESSED PAD1 DPADRIGHT
            THEN
                TEMP_1 = 128
            END
            IF IS_BUTTON_PRESSED PAD1 DPADUP
            THEN
                TEMP_2 = -128
            END
            IF IS_BUTTON_PRESSED PAD1 DPADDOWN
            THEN
                TEMP_2 = 128
            END
        END
        
        GET_VAR_POINTER gSaveSafeDrugQtyBegin TEMP_3
        GET_VAR_POINTER gSaveDrugQtyBegin TEMP_4
        
        IF NOT TEMP_1 == 0
        THEN
            IF VAR_6 == 0
            THEN
                IF TEMP_1 > 64
                THEN
                    VAR_5 += 1
                    VAR_6 = 1
                END
                IF TEMP_1 < -64
                THEN
                    VAR_5 -= 1
                    VAR_6 = 1
                END
                IF VAR_5 > MAX_DRUG
                THEN
                    VAR_5 = MIN_DRUG
                END
                IF VAR_5 < MIN_DRUG
                THEN
                    VAR_5 = MAX_DRUG
                END
            END
        ELSE
            IF VAR_6 == 1
            THEN
                VAR_6 = 0
            END
        END
        
        IF IS_BUTTON_PRESSED PAD1 CROSS
        THEN
            IF VAR_6 == 0
            THEN
                IF 0019:   TEMP_4(VAR_5,4s) > 0
                THEN
                    005A: TEMP_3(VAR_5,4s) += TEMP_4(VAR_5,4s)
                    0006: TEMP_4(VAR_5,4s) = 0
                    TEMP_2 = 0
                END
                VAR_6 = CROSS
            END
        ELSE
            IF VAR_6 == CROSS
            THEN
                VAR_6 = 0
            END
        END
        IF IS_BUTTON_PRESSED PAD1 SQUARE
        THEN
            IF VAR_6 == 0
            THEN
                IF 0019:   TEMP_3(VAR_5,4s) > 0
                THEN
                    005A: TEMP_4(VAR_5,4s) += TEMP_3(VAR_5,4s)
                    0006: TEMP_3(VAR_5,4s) = 0
                    TEMP_2 = 0
                END
                VAR_6 = SQUARE
            END
        ELSE
            IF VAR_6 == SQUARE
            THEN
                VAR_6 = 0
            END
        END
        
        IF NOT TEMP_2 == 0
        THEN
            IF VAR_6 == 0
            THEN
                IF TEMP_2 > 64
                THEN
                    IF 0019:   TEMP_3(VAR_5,4s) > 0
                    THEN
                        000E: TEMP_3(VAR_5,4s) -= 1
                        000A: TEMP_4(VAR_5,4s) += 1
                    END
                    VAR_6 = 2
                END
                IF TEMP_2 < -64
                THEN
                    IF 0019:   TEMP_4(VAR_5,4s) > 0
                    THEN
                        000E: TEMP_4(VAR_5,4s) -= 1
                        000A: TEMP_3(VAR_5,4s) += 1
                    END
                    VAR_6 = 2
                END
            END
        ELSE
            IF VAR_6 == 2
            THEN
                VAR_6 = 0
            END
        END
        
        CALL @DrawStashBox 0
        CALL @DrawDrugRow 5 TEMP_3 120.0 180.0 64.0 VAR_5
        
        CALL @DrawPocketBox 0
        CALL @DrawDrugRow 5 TEMP_4 120.0 320.0 64.0 VAR_5
        
        GET_LABEL_POINTER @TempMemory TEMP_1
        STRING_FORMAT TEMP_1 "~k~~GO_LEFT~ ~k~~GO_RIGHT~ select drug.~N~~k~~GO_FORWARD~ ~k~~GO_BACK~ add/take drug."
        SET_TEXT_FONT 2
        SET_TEXT_EDGE ON 0 0 0 255
        SET_TEXT_SIZE 320.0
        SET_TEXT_PROPORTIONAL TRUE
        CALL @DisplayText 3 50.0 390.0 TEMP_1
        STRING_FORMAT TEMP_1 "%s" "~k~~PED_SPRINT~ add all.~N~~k~~PED_JUMPING~ take all.~N~~k~~PED_FIREWEAPON~ Abuse.~N~~k~~VEHICLE_ENTER_EXIT~ exit."
        SET_TEXT_FONT 2
        SET_TEXT_EDGE ON 0 0 0 255
        SET_TEXT_RIGHT_JUSTIFY ON
        SET_TEXT_SIZE 640.0
        SET_TEXT_PROPORTIONAL TRUE
        CALL @DisplayText 3 590.0 390.0 TEMP_1
        
        IF IS_BUTTON_PRESSED PAD1 CIRCLE
        THEN
            IF VAR_6 == 0
            THEN
                IF 0019:   TEMP_4(VAR_5,4s) > 0
                THEN
                    000E: TEMP_4(VAR_5,4s) -= 1
                    CALL @IncDrugTakenStat 1 VAR_5
                    CALL @Async 2 @GUI_TakeDrug VAR_5
                END
                VAR_6 = CIRCLE
                GOSUB @System_SafeHouse_ExitStashDialog
            END
        ELSE
            IF VAR_6 == CIRCLE
            THEN
                VAR_6 = 0
            END
        END
        
        IF IS_BUTTON_PRESSED PAD1 TRIANGLE
        THEN
            IF VAR_6 == 0
            THEN
                VAR_6 = TRIANGLE
            END
        ELSE
            IF VAR_6 == TRIANGLE
            THEN
                GOSUB @System_SafeHouse_ExitStashDialog
            END
        END
    ELSE
        GOSUB @System_SafeHouse_ExitStashDialog
    END
END
RETURN

:System_SafeHouse_ExitStashDialog
VAR_3 = 3
DISPLAY_RADAR TRUE
RETURN

{********************************************}

// Some functions...

:DrawDrugRow{\__(pDrugQtyArray,_fXPos,_fYPos,_fScale,_nSelectedDrug,_Transparency)__}
IF 5@ == 0
THEN 10@ = 130
ELSE 0085: 10@ = 5@
END
FOR 5@ = MIN_DRUG TO MAX_DRUG
    0085: 6@ = 10@
    0A8E: 7@ = 5@ + 1
    IF 8019:   NOT 0@(5@,4s) > 0
    THEN
        7@ += MAX_DRUG
        7@ += 1
    END
    IF 003B:   4@ == 5@
    THEN 6@ = 255
    END
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    
    DRAW_SPRITE 7@ 1@ 2@ 3@ 3@ 255 255 255 6@

    IF 0019:   0@(5@,4s) > 0
    THEN
        0087: 8@ = 1@
        0087: 9@ = 2@
        8@ -= 16.0
        9@ += 20.0
        GET_STRING_WIDTH_WITH_NUMBER "NUMBER" 0@(5@,4s) 7@
        008F: 11@ = int_to_float 7@
        11@ += 14.0
        DRAW_SPRITE 13 8@ 9@ 11@ 20.0 200 200 200 240
        //8@ -= 4.0
        9@ -= 6.0
        SET_TEXT_FONT 1
        SET_TEXT_CENTER 1
        SET_TEXT_EDGE ON 0 0 0 255
        DISPLAY_TEXT_WITH_NUMBER 8@ 9@ "NUMBER" 0@(5@,4s)
    END
    005B: 1@ += 3@
    1@ += 16.0
END
RET 0

:DrawStashBox
SET_SPRITES_DRAW_BEFORE_FADE TRUE
DRAW_RECT_WITH_TITLE 40.0 120.0 600.0 240.0 "_BMSHD" 0
RET 0

:DrawPocketBox
SET_SPRITES_DRAW_BEFORE_FADE TRUE
DRAW_RECT_WITH_TITLE 40.0 260.0 600.0 380.0 "_BMPKT" 0
RET 0

{********************************************}

// Back to System...

:System_Save
// 1@  - Save Slot
GET_LABEL_POINTER @TempMemory 2@
STRING_FORMAT 2@ "cleo\BlackMarket\save\save%d.bms" 1@
IF OPEN_FILE 2@ "w" 3@
THEN
    0085: SaveDataIndex = gSaveDataPointer
    4@ = VERSION_LONG
    WRITE_TO_FILE 3@ 4 4@
    0A90: 5@ = 4 * NUM_SAVE_VARS
    WRITE_TO_FILE 3@ 5@ gSaveDataBegin
    CLOSE_FILE 3@
    
    PRINTINT2 "BMM_SAVED" 1@
END
RET 0

:System_Load
// 1@ - Load Slot
GET_LABEL_POINTER @TempMemory 2@
STRING_FORMAT 2@ "cleo\BlackMarket\save\save%d.bms" 1@
IF OPEN_FILE 2@ "r" 3@
THEN
    READ_FROM_FILE 3@ 4 4@
    GET_FILE_SIZE 3@ 4@
    4@ -= 4
    0A90: 5@ = 4 * NUM_SAVE_VARS
    IF 003B:   4@ == 5@
    THEN
        0085: SaveDataIndex = gSaveDataPointer
        GET_VAR_POINTER gSaveDataBegin 4@
        WHILE NOT IS_END_OF_FILE_REACHED 3@
            READ_FROM_FILE 3@ 4 6@
            WRITE_MEMORY 4@ 4 6@ 0
            4@ += 4
        END
    ELSE
        PRINTSTRING "BMM_SAVE_FILE_INCOMPATIBLE"
    END
    CLOSE_FILE 3@
    
    PRINTINT2 "BMM_LOADED_SAVE" 1@
END
RET 0

{********************************************}

{$I gui.sc}

{********************************************}

:HelpDisplay
{
  SVar Indexes
     0  - [In] Help Diplsay Message
     1  - Message Display Stage
}

IF CALL @IsDebug 0
THEN
    RET 1 TRUE
END

IF OR
    IS_HELP_MESSAGE_BEING_DISPLAYED
    NOT IS_PLAYER_PLAYING player1
    NOT IS_PLAYER_SCRIPT_CONTROL_ON player1
THEN
    RET 1 FALSE
END

0085: 1@ = SVar[-1@]
000A: SVar[-1@] += 1

IF 0039:   SVar[SVar] == HELPDISP_SELL_DRUGS
THEN
    IF 1@ == 0
    THEN
        PRINT_HELP_STRING "You now have drugs in your drug inventory."
    END
    IF 1@ == 1
    THEN
        PRINT_HELP_STRING "Peds may now approach you if you have the drugs they want and you're stood still."
    END
    IF 1@ == 2
    THEN
        PRINT_HELP_STRING "You can respond positively to make a small-time deal. The selling price is determined by reputation and respect."
    END
    IF 1@ == 3
    THEN
        PRINT_HELP_STRING "Hold ~k~~PED_FIREWEAPON_ALT~ and press ~k~~CONVERSATION_YES~ to view your drug inventory"
        RET 1 TRUE
    END
    RET 1 FALSE
END

IF 0039:   SVar[SVar] == HELPDISP_ABUSE_DRUGS
THEN
    IF 1@ == 0
    THEN
        PRINT_HELP_STRING "This is your drug inventory, you can use ~k~~CONVERSATION_YES~ and ~k~~CONVERSATION_NO~ to select drugs."
    END
    IF 1@ == 1
    THEN
        PRINT_HELP_STRING "With a drug selected, you can use ~k~~GROUP_CONTROL_FWD~ to abuse it."
        RET 1 TRUE
    END
    RET 1 FALSE
END
RET 1 FALSE

{********************************************}

:Abuse
HEX
0A00 01 @InitProc
END
{
    VAR_1  - Last Time Sync'd (Weed)
    VAR_2  - fWavyness
    VAR_3  - Green-ness
    VAR_4  - Blurryness
    VAR_5  - Last Drug Drop
    VAR_6  - Puke Particle
    VAR_7  - OD Stage
}

IF NOT IS_PLAYER_PLAYING player1
THEN
    CALL @ClearAllDrugLevels 0
    GOTO @Abuse_End
END

WRITE_MEMORY 0x8CBA6C 4 0.5 FALSE       // revert audio speed to default in case we're not messing with it...

GET_GAME_TIMER TEMP_1
0062: TEMP_1 -= gTimeOverdosed

IF 0039:   gHasOverdosed == 0
THEN
    0087: TEMP_1 = gSaveStatDrugExperience
    TEMP_1 /= 5.0
    0093: TEMP_2 = int_to_float gDrugLevelInSystem
    0063: TEMP_2 -= TEMP_1
    IF TEMP_2 < 25.0
    THEN
        IF TEMP_2 > 0
        THEN
            0006: gHasOverdosed = 1
            VAR_7 = 0
            GET_GAME_TIMER gTimeOverdosed
            SHUT_CHAR_UP scplayer TRUE
            DAMAGE_CHAR scplayer 10 FALSE
            REQUEST_ANIMATION "FOOD"
            IF NOT HAS_ANIMATION_LOADED "BLACKMARKET"
            THEN REQUEST_ANIMATION "BLACKMARKET"
            END
            000A: gNumAnimReferences += 1
        END
    ELSE
        0006: gHasOverdosed = 1
        VAR_7 = -1                          // K/O
        GET_GAME_TIMER gTimeOverdosed
    END
ELSE
    IF VAR_7 == -1
    THEN
        DO_FADE FADE_OUT 3000
        VAR_7 = -2
    ELSE
        IF VAR_7 == -2
        THEN
            IF NOT GET_FADING_STATUS
            THEN
                SET_CHAR_HEALTH scplayer 0
                0006: gHasOverdosed = 0
                VAR_7 = 0
                GET_GAME_TIMER gTimeOverdosed
            END
        END
    END
    IF VAR_7 == 0
    THEN
        IF TEMP_1 >= 20000
        THEN
            IF AND
                HAS_ANIMATION_LOADED "FOOD"
                HAS_ANIMATION_LOADED "BLACKMARKET"
            THEN
                GET_GAME_TIMER gTimeOverdosed
                DIM_SCREEN ON -1
                
                IF IS_CHAR_ON_FOOT scplayer
                THEN
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.355 -0.116 -0.048 STORE_TO TEMP_2 TEMP_3 TEMP_4  
                    CREATE_PARTICLE "PUKE" TEMP_2 TEMP_3 TEMP_4  1 VAR_6 
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE scplayer "EAT_VOMIT_P" "FOOD" 1004.0 0 0 0 0 -1
                    VAR_7 = 1
                ELSE
                    VAR_7 = 3
                    FOR TEMP_2 = MIN_DRUG TO MAX_DRUG
                        CALL @DecDrugLevel 2 TEMP_2 5
                    END
                    0006: gHasOverdosed = 0
                END
            END
        END
    ELSE
        IF TEMP_1 >= 2000
        THEN
            IF IS_CHAR_PLAYING_ANIM scplayer "EAT_VOMIT_P"
            THEN
                GET_CHAR_ANIM_CURRENT_TIME scplayer "EAT_VOMIT_P" TEMP_2
            ELSE
                TEMP_2 = 1.0
            END
            IF VAR_7 == 1
            THEN
                IF TEMP_2 >= 0.463
                THEN
                    SAY_AMBIENT_SPEECH scplayer 353 TEMP_3
                    VAR_7 = 2
                END
            ELSE
                IF VAR_7 == 2
                THEN
                    IF TEMP_2 >= 0.52
                    THEN
                        VAR_7 = 3
                        FOR TEMP_2 = MIN_DRUG TO MAX_DRUG
                            CALL @DecDrugLevel 2 TEMP_2 5
                        END
                        MARK_PARTICLE_AS_NO_LONGER_NEEDED VAR_6
                        GET_GAME_TIMER gTimeOverdosed
                    END
                ELSE
                    IF VAR_7 == 3
                    THEN
                        GET_GAME_TIMER TEMP_1
                        0062: TEMP_1 -= gTimeOverdosed
                    END
                    IF VAR_7 == 4
                    THEN
                        REMOVE_ANIMATION "FOOD"
                        IF 8019:   NOT gNumAnimReferences > 0
                        THEN
                            REMOVE_ANIMATION "BLACKMARKET"
                        END
                        CALL @Abuse_SetGameSpeed 1 1.0
                        DIM_SCREEN OFF -1
                        SHUT_CHAR_UP scplayer FALSE
                        0006: gHasOverdosed = 0
                    END
                END
            END
        END
    END
    
    IF AND
        VAR_7 >= 1
        NOT VAR_7 == 4
    THEN
        0093: TEMP_2 = int_to_float TEMP_1
        TEMP_2 /= 3000.0
        TEMP_2 *= 0.5
        IF TEMP_2 > 0.5
        THEN TEMP_2 = 0.5
        END
        
        IF VAR_7 == 3
        THEN
            TEMP_3 = 0.5
            005B: TEMP_3 += TEMP_2
        ELSE
            TEMP_3 = 1.0
            0063: TEMP_3 -= TEMP_2
        END
        
        CALL @Abuse_SetGameSpeed 1 TEMP_3
        
        TEMP_3 = 255.0
        006B: TEMP_3 *= TEMP_2
        0092: TEMP_3 = float_to_int TEMP_3
        
        IF VAR_7 == 3
        THEN
            0A8F: TEMP_3 = 128 - TEMP_3
        END
        
        IF TEMP_3 <= 0
        THEN
            DIM_SCREEN ON 0
        ELSE
            IF TEMP_3 >= 127
            THEN
                DIM_SCREEN ON 127
            ELSE
                DIM_SCREEN ON TEMP_3
            END
        END
        
        IF VAR_7 == 3
        THEN
            IF TEMP_2 >= 0.5
            THEN
                VAR_7 = 4
            END
        END
    END
END

IF 0021:   gSmokeBlown > 0.0
THEN
    GET_CHAR_COORDINATES scplayer TEMP_1 TEMP_2 TEMP_3
    GET_DISTANCE_BETWEEN_COORDS_3D TEMP_1 TEMP_2 TEMP_3 AND gSmokeBlownX gSmokeBlownY gSmokeBlownZ STORE_TO TEMP_4
    IF TEMP_4 > 20.0
    THEN
        0007: gSmokeBlown = 0
    ELSE
        GET_CHAR_SPEED scplayer TEMP_1
        GET_ACTIVE_INTERIOR TEMP_2
        IF TEMP_2 == 0
        THEN
            TEMP_1 /= 400.0
            0063: gSmokeBlown -= TEMP_1
            007F: gSmokeBlown -= frame_delta_time * 0.0008
        ELSE
            TEMP_1 /= 1600.0
            0063: gSmokeBlown -= TEMP_1
            007F: gSmokeBlown -= frame_delta_time * 0.0002
        END
        
        GET_GAME_TIMER TEMP_1
        0062: TEMP_1 -= gLastSmokeEmission
        IF TEMP_1 > 250
        THEN
            IF TEMP_2 == 0
            THEN
                TEMP_7 = 0.005
            ELSE
                TEMP_7 = 0.01
            END
            006B: TEMP_7 *= gSmokeBlown
            IF TEMP_7 > 1.0
            THEN
                TEMP_7 = 1.0
            END
            IF TEMP_2 == 0
            THEN
                FOR TEMP_3 = 0 TO 8
                    GENERATE_RANDOM_FLOAT_IN_RANGE -6.0 6.0 TEMP_1
                    GENERATE_RANDOM_FLOAT_IN_RANGE -6.0 6.0 TEMP_2
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer TEMP_1 TEMP_2 -0.5 TEMP_4 TEMP_5 TEMP_6
                    READ_MEMORY 0xC813E0 4 FALSE TEMP_8     // windvelX
                    READ_MEMORY 0xC813E4 4 FALSE TEMP_9     // windvelY
                    CREATE_SMOKE TEMP_4 TEMP_5 TEMP_6 VELOCITY TEMP_8 TEMP_9 0.15 RGBA 2.0 2.0 2.0 TEMP_7 SIZE 0.5 LAST_FACTOR 2.0
                END
            ELSE
                FOR TEMP_3 = 0 TO 8
                    GENERATE_RANDOM_FLOAT_IN_RANGE -6.0 6.0 TEMP_1
                    GENERATE_RANDOM_FLOAT_IN_RANGE -6.0 6.0 TEMP_2
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer TEMP_1 TEMP_2 -0.5 TEMP_4 TEMP_5 TEMP_6
                    CREATE_SMOKE TEMP_4 TEMP_5 TEMP_6 VELOCITY 0 0 0.15 RGBA 2.0 2.0 2.0 TEMP_7 SIZE 0.5 LAST_FACTOR 2.0
                END
            END
            GET_GAME_TIMER gLastSmokeEmission
        END
    END
ELSE
    IF 0023:   0 > gSmokeBlown
    THEN 0007: gSmokeBlown = 0
    END
END

VAR_2 = 0
VAR_3 = 0
VAR_4 = 0
TEMP_4 = 0.0

IF 803B:   NOT gWeedLevel == gWeedLevelInSystem
THEN
    GET_GAME_TIMER TEMP_6
    0062: TEMP_6 -= VAR_1
    IF TEMP_6 > 2000
    THEN
        IF 001D:   gWeedLevel > gWeedLevelInSystem
        THEN
            CALL @IncDrugLevelInSystem 2 WEED 1
        ELSE
            CALL @DecDrugLevelInSystem 2 WEED 1
        END
        
        GET_GAME_TIMER VAR_1
    END
ELSE
    GET_GAME_TIMER VAR_1
END

GET_GAME_TIMER TEMP_1
0A8F: TEMP_2 = TEMP_1 - VAR_5
IF TEMP_2 > 20000
THEN
    IF 0019:   gWeedLevel > 0
    THEN
        0A8F: TEMP_2 = TEMP_1 - gWeedTimeLastTook
        IF TEMP_2 > 10000
        THEN
            CALL @DecDrugLevel 1 WEED
        END
    END
    
    GET_GAME_TIMER VAR_5
END

IF 0019:   gWeedLevelInSystem > 0
THEN
    008F: gWeedEffectLevel = int_to_float gWeedLevelInSystem
    0017: gWeedEffectLevel /= 16.0
    
    TEMP_1 = 127.0
    006B: TEMP_1 *= gWeedEffectLevel
    0092: TEMP_1 = float_to_int TEMP_1
    005A: VAR_4 += TEMP_1
    
    TEMP_1 = 1.5
    006B: TEMP_1 *= gWeedEffectLevel
    005B: VAR_2 += TEMP_1
    
    IF 0021:   gWeedEffectLevel > 0.75
    THEN
        0087: TEMP_2 = gWeedEffectLevel
        TEMP_2 -= 0.75
        TEMP_1 = 100.0
        006B: TEMP_1 *= TEMP_2
        0092: TEMP_1 = float_to_int TEMP_1
        005A: VAR_3 += TEMP_1
    END
ELSE
    0006: gWeedLevelInSystem = 0
    0007: gWeedEffectLevel = 0.0
END

{IF //VAR_1 > 15000
    VAR_1 > 1000 // (debug)
THEN
    SET_PLAYER_MOOD player1 PLAYER_MOOD_CALM 999999
    GET_GAME_TIMER TEMP_5
    0A8F: TEMP_6 = TEMP_5 - VAR_1
END}

IF VAR_3 > 150
THEN
    VAR_3 = 150
END

IF VAR_4 > 140
THEN
    VAR_4 = 140
END

IF VAR_2 > 1.0
THEN
    VAR_2 = 1.0
END

//PRINT_FORMATTED "%g %d" 1 gWeedEffectLevel gWeedLevelInSystem

IF VAR_3 > 0
THEN
    0A8F: TEMP_1 = 255 - VAR_3
    0A8F: TEMP_2 = 255 - VAR_3
    //0A8E: TEMP_3 = 255 + VAR_3
    TEMP_3 = 255
    CALL @Abuse_SetBlurRGB 3 TEMP_1 TEMP_3 TEMP_2
    CALL @Abuse_SetBlurIntensity 1 VAR_4
ELSE
    CALL @Abuse_ResetBlur 0
END

IF VAR_2 > 0.0
THEN
    GET_CHAR_SPEED scplayer TEMP_1
    READ_MEMORY 0xC73C58 4 FALSE TEMP_2
    
    IF TEMP_1 >= 40.0
    THEN
        TEMP_1 = 1.0
    ELSE
        IF TEMP_1 <= 0.05
        THEN
            TEMP_1 = 0.05
        ELSE
            TEMP_1 /= 40.0
        END
    END
    
    TEMP_1 *= 0.25
    0087: TEMP_3 = TEMP_1
    0063: TEMP_3 -= TEMP_2
    ABSF_L TEMP_3
    
    IF TEMP_3 > 0.00025
    THEN
        TEMP_3 *= 0.0125
        IF 001D:   TEMP_1 > TEMP_2
        THEN
            007B: TEMP_2 += frame_delta_time * TEMP_3
        ELSE
            0081: TEMP_2 -= frame_delta_time * TEMP_3
        END
        IF IS_CHAR_SHOOTING scplayer
        THEN
            TEMP_2 *= 1.7   // jolt
        END
        IF TEMP_2 > 1.5
        THEN
            TEMP_2 = 1.5
        END
    ELSE
        0087: TEMP_2 = TEMP_1
    END
    
    006B: TEMP_2 *= VAR_2
    //PRINT_FORMATTED "%g" 1 TEMP_2
    WRITE_MEMORY 0xC73C58 4 TEMP_2 FALSE        // wavy motion
END

GENERATE_RANDOM_FLOAT_IN_RANGE 0.7 0.72 TEMP_1

//CALL @Abuse_GetBlur

//CALL_FUNCTION 0x7030A0 1 1 TEMP_1
//FORCE_WEATHER_NOW 188                     // (shrooms)
//SET_TIME_SCALE 0.75
//DIM_SCREEN 1 TO 100
//WRITE_MEMORY 0x8CBA6C 4 0.9 FALSE           // audio speed
:Abuse_End
GOSUB @EndProc
RET 0

:Abuse_LowerGameSpeed{\__[fSpeed]__}
READ_MEMORY 0xB7CB64 4 FALSE 1@                 // time scale
IF 0025:   1@ > 0@
THEN
    SET_TIME_SCALE 0@
    WRITE_MEMORY 0x8CBA6C 4 0@ FALSE            // audio speed
END
RET 0

:Abuse_SetGameSpeed{\__[fSpeed]__}
SET_TIME_SCALE 0@
WRITE_MEMORY 0x8CBA6C 4 0@ FALSE                // audio speed
RET 0

:Abuse_GetBlurIntensity{\__[fIntensity]__}
0@ = 0xC402C8
RET 1 0@(0@,4s)

:Abuse_SetBlurRGB{\__(ucR,_ucG,_ucB)__}
WRITE_MEMORY 0x70332B 4 2@ TRUE
WRITE_MEMORY 0x703330 4 1@ TRUE
WRITE_MEMORY 0x703339 4 0@ TRUE
RET 0

:Abuse_SetBlurIntensity{\__(ucIntensity)__}
WRITE_MEMORY 0x8D5100 1 1 FALSE             // turn it on
WRITE_MEMORY 0x8D5190 4 0.0 FALSE           // min blur value

WRITE_MEMORY 0x527971 1 1 TRUE              // force motion blur
WRITE_MEMORY 0x704DCD 4 0x12C TRUE          // force motion blur when not in car
//WRITE_MEMORY 0xC402C8 4 0@ FALSE            // write motion blur value
//WRITE_MEMORY 0xC4016C 4 0@ FALSE            // write motion blur value
//WRITE_MEMORY 0xC40170 4 0@ FALSE            // write motion blur value
WRITE_MEMORY 0x8D5104 1 0@ FALSE           // write motion blur alpha (127 is the peak intensity)
RET 0

:Abuse_ResetBlur
WRITE_MEMORY 0x8D5190 4 0.6 FALSE           // restore min blur value
WRITE_MEMORY 0x527971 1 0 TRUE              // un-force motion blur
WRITE_MEMORY 0x704DCD 4 0x126 TRUE          // un-force motion blur when not in car
WRITE_MEMORY 0x8D5104 1 36 FALSE            // restore motion blur alpha (0x24 - default)

// restore rgb
WRITE_MEMORY 0x70332B 4 255 TRUE
WRITE_MEMORY 0x703330 4 255 TRUE
WRITE_MEMORY 0x703339 4 255 TRUE
RET 0

{********************************************}

:Submissions
HEX
0100 01 @InitProc
END
IF $ONMISSION == 0
THEN
    IF IS_PLAYER_PLAYING player1
    THEN
        IF AND
            NOT ARE_HELP_MESSAGES_BLOCKED
            NOT IS_BIT_SET_G $GFAGNT_Bools 1
        THEN
            IF IS_CHAR_IN_MODEL scplayer #BMX
            THEN
                IF $ONMISSION_Courier == 0
                THEN
                    IF DOES_FILE_EXIST "cleo\BlackMarket\mission\submiss1.cm"
                    THEN
                        IF VAR_1 == 0
                        THEN
                            IF NOT IS_HELP_MESSAGE_BEING_DISPLAYED
                            THEN
                                PRINT_HELP_STRING "Press ~k~~TOGGLE_SUBMISSIONS~ to toggle drug peddling missions on or off."
                                VAR_1 = 1
                            END
                        END
                        GET_CONTROLLER_MODE TEMP_1
                        IF NOT TEMP_3 == 3
                        THEN
                            IF IS_BUTTON_PRESSED PAD1 RIGHTSHOCK
                            THEN
                                // TODO: Finish for 1.0.2
                                PRINT_BIG '_BMCAR' 3000 5
                                LAUNCH_CUSTOM_MISSION "cleo\BlackMarket\mission\submiss1"
                            END
                        END
                    END
                END
            END
        END
    END
END
GOSUB @EndProc
RET 0

{********************************************}

{}:Async_Data
0000:   // Memory
0000:
0000:   // Num Slots
0000:

{}:Async_Sys              // Asynchronous requests
HEX
0000 01 @InitProc
END
TEMP_1 = @Async_Data
IF 0039:   TEMP_1(TEMP_1,4s) == 0
THEN
    CALL @Async_Alloc 0
ELSE
    0085: TEMP_2 = TEMP_1(TEMP_1,4s)
    0085: TEMP_5 = TEMP_1(-1@,4s)
    FOR TEMP_3 = 0 TO TEMP_5
        IF 0039:    TEMP_2(TEMP_2,4s) == 1
        THEN
            GET_VAR_POINTER TEMP_2(CAsync_VARS_BEGIN,4s) TEMP_3
            CALL TEMP_2(CAsync_FUNCTION,4s) 1 TEMP_3 TEMP_4
            IF TEMP_4 == TRUE
            THEN
                0006: TEMP_2(TEMP_2,4s) = 0
            END
        END
        TEMP_2 += CAsync_SIZE
    END
END
GOSUB @EndProc
RET 0

{}:Async_Alloc
0@ = @Async_Data
1@ = CAsync_SIZE
0085: 2@ = 0@(-1@,4s)
IF NOT 2@ > 0
THEN 2@ = 0
END
IF 0039:   0@(-1@,4s) == 0
THEN 0006: 0@(-1@,4s) = 8
ELSE 0012: 0@(-1@,4s) *= 2
END
006A: 1@ *= 0@(-1@,4s)
IF ALLOCATE_MEMORY 1@ 3@
THEN
    WRITE_MEMORY 3@ 1@ 0 FALSE
    0085: 4@ = 0@(0@,4s)
    IF NOT 4@ == 0
    THEN
        FOR 5@ = 0 TO 2@
            0085: 3@(5@,4s) = 4@(5@,4s)
        END
    END
    0085: 0@(0@,4s) = 3@
    RETURN_TRUE
ELSE
    0006: 0@(-1@,4s) = 0
END
RET 0

:Async{__(@Func,_Params...)__}
14@ = @Async_Data
0085: 15@ = 14@(14@,4s)
WHILE TRUE
    0085: 19@ = 14@(-1@,4s)
    FOR 16@ = 0 TO 19@
        IF 0039:    15@(15@,4s) == 0
        THEN
            0006: 15@(15@,4s) = 1
            0085: 15@(CAsync_FUNCTION,4s) = 0@
            GET_VAR_POINTER 15@(CAsync_VARS_BEGIN,4s) 17@
            FOR 18@ = 0 TO CAsync_NUM_VARS
                0085: 17@(18@,4s) = 1@(18@,1i)
            END
            RET 0
        END
        15@ += CAsync_SIZE
    END
    CALL @Async_Alloc 0
    ELSE_GOTO BREAK
END
RET 0

{********************************************}

:Market                 // Processes the live market
HEX
0100 01 @InitProc
END
{$I market.sc}
RET 0

{********************************************}

:DealerHook             // Hooks the DEALER script
HEX
0800 01 @InitProc
END
{
    VAR_1       (INT)           Selected Scoring Item Index
    VAR_2       (INT)           Direction Button Used
    VAR_3       (INT)           Button Down
    VAR_4       (ARRAY INT)     Scoring Quantities (sizeof: MAX_DRUG)
}
IF IS_PLAYER_PLAYING player1
THEN
    // Check we got some dealers about to hack
    GET_NUMBER_OF_SCRIPT_INSTANCES_OF_STREAMED_SCRIPT SCRIPT_DEALER STORE_TO TEMP_1
    IF NOT TEMP_1 == 0
    THEN
        GOSUB @DealerHook_Hook
    END
END
{0006: gNumProcVars = 2
000A: gNumProcVars += MAX_DRUG}
GOTO @DealerHook_End

:DealerHook_Hook
GET_LABEL_POINTER @TempMemory TEMP_2
IF timera > 1000
THEN
    // Search for 'dealer' scripts
    STRING_FORMAT TEMP_2 "dealer"
    IF CALL @CTheScripts__GetNextActiveScriptNamed 2 1 TEMP_2 >> TEMP_1
    THEN
        WHILE TRUE
            0085: TEMP_3 = gCurrentProcPointer
            0085: TEMP_1(-4@,4s) = TEMP_3(-4@,4s)
            GET_LABEL_POINTER @DEALER TEMP_1(-5@,4s)
            CALL @CTheScripts__GetNextActiveScriptNamed 2 0 TEMP_2 TEMP_1
            ELSE_GOTO BREAK
        END
    END
    timera = 0
END

// Search for 'bmdeal' scripts
STRING_FORMAT TEMP_2 "bmdeal"
IF CALL @CTheScripts__GetNextActiveScriptNamed 2 1 TEMP_2 >> TEMP_1
THEN
    WHILE TRUE
        CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_DEAL2PLAYER TEMP_3
        IF AND
            NOT TEMP_3 == 0
            NOT TEMP_3 == 3
        THEN
            CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_CHAR TEMP_2
            IF NOT IS_CHAR_DEAD TEMP_2
            THEN
                GOSUB @DealerHook_DoDeal
            END
            GET_LABEL_POINTER @TempMemory TEMP_2
            STRING_FORMAT TEMP_2 "bmdeal"
        END
        CALL @CTheScripts__GetNextActiveScriptNamed 2 0 TEMP_2 TEMP_1
        ELSE_GOTO BREAK
    END
END
RETURN

:DealerHook_DoDeal
IF TEMP_3 == 2
THEN
    // Scoring controls
    GET_PAD_STATE 0 LEFTSTICKX STORE_TO TEMP_2
    GET_PAD_STATE 0 LEFTSTICKY STORE_TO TEMP_3
    
    IF IS_PC_USING_JOYPAD
    THEN
        IF IS_BUTTON_PRESSED 0 DPADLEFT
        THEN
            TEMP_2 = -128
        END
        IF IS_BUTTON_PRESSED 0 DPADRIGHT
        THEN
            TEMP_2 = 128
        END
        IF IS_BUTTON_PRESSED 0 DPADUP
        THEN
            TEMP_3 = -128
        END
        IF IS_BUTTON_PRESSED 0 DPADDOWN
        THEN
            TEMP_3 = 128
        END
    END
    
    CALL @GetDealerScriptTotalDrugQuantity 1 TEMP_1 TEMP_4
    
    IF AND
        TEMP_4 > 0
        VAR_2 == 0
    THEN
        IF TEMP_2 > 64
        THEN
            REPEAT
                IF VAR_1 >= MAX_DRUG
                THEN VAR_1 = 0
                ELSE VAR_1 += 1
                END
                CALL @GetDealerScriptDrugQuantity 2 TEMP_1 VAR_1 TEMP_4
            UNTIL TEMP_4 > 0
            VAR_2 = 1
        END
        IF TEMP_2 < -64
        THEN
            REPEAT
                IF VAR_1 <= 0
                THEN VAR_1 = MAX_DRUG
                ELSE VAR_1 -= 1
                END
                CALL @GetDealerScriptDrugQuantity 2 TEMP_1 VAR_1 TEMP_4
            UNTIL TEMP_4 > 0
            VAR_2 = 2
        END
        
        CALL @GetDealerScriptDrugQuantity 2 TEMP_1 VAR_1 TEMP_4
        0062: TEMP_4 -= VAR_4(VAR_1,6i)
        
        IF 0019:   VAR_4(VAR_1,6i) > 0
        THEN
            IF TEMP_3 > 64
            THEN
                000E: VAR_4(VAR_1,6i) -= 1
                VAR_2 = 3
            END
        END
        IF TEMP_4 > 0
        THEN
            CALL @GetTotalDrugQuantity 0 TEMP_5
            FOR TEMP_6 = MIN_DRUG TO MAX_DRUG
                005A: TEMP_5 += VAR_4(TEMP_6,6i)
            END
            IF TEMP_5 < MAX_DRUG_QTY
            THEN
                IF TEMP_3 < -64
                THEN
                    000A: VAR_4(VAR_1,6i) += 1
                    VAR_2 = 3
                END
            ELSE
                PRINT_STRING "You cannot carry any more drugs." 4000
            END
        END
    ELSE
        IF NOT VAR_2 == 3
        THEN
            IF NOT VAR_2 == 0
            THEN
                IF AND
                    TEMP_2 > -64
                    TEMP_2 < 64
                THEN
                    VAR_2 = 0
                END
            END
        ELSE
            IF AND
                TEMP_3 > -64
                TEMP_3 < 64
            THEN
                VAR_2 = 0
            END
        END
    END
END

TEMP_7 = 0

FOR TEMP_2 = MIN_DRUG TO MAX_DRUG
    CALL @GetDealerScriptDrugQuantity 2 TEMP_1 TEMP_2 TEMP_3
    
    IF TEMP_3 > 0
    THEN 0A8E: TEMP_4 = TEMP_2 + 1
    ELSE
        IF 003B:   VAR_1 == TEMP_2
        THEN
            IF VAR_2 == 2
            THEN VAR_1 -= 1
            ELSE VAR_1 += 1
            END
        END
        0A8E: TEMP_4 = TEMP_2 + 7
    END
    
    0093: TEMP_5 = integer TEMP_2 to_float
    TEMP_5 *= 80.0
    TEMP_5 += 120.0
    
    IF 003B:   VAR_1 == TEMP_2
    THEN TEMP_6 = 255
    ELSE TEMP_6 = 140
    END
    
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE TEMP_4 TEMP_5 394.0 64.0 64.0 255 255 255 TEMP_6
    
    IF TEMP_3 > 0
    THEN
        TEMP_5 -= 16.0
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE 13 TEMP_5 414.0 20.0 20.0 200 200 200 255
        TEMP_5 -= 5.0
        SET_TEXT_FONT 1
        SET_TEXT_EDGE ON 0 0 0 255
        0A8F: TEMP_4 = TEMP_3 - VAR_4(TEMP_2,6i)
        DISPLAY_TEXT_WITH_NUMBER TEMP_5 408.0 "NUMBER" TEMP_4
    END
    
    IF 001B:   0 > VAR_4(TEMP_2,6i)
    THEN
        0006: VAR_4(TEMP_2,6i) = 0
    END
    IF 001D:   VAR_4(TEMP_2,6i) > TEMP_5
    THEN
        0085: VAR_4(TEMP_2,6i) = TEMP_5
    END
    
    IF 003B:   VAR_1 == TEMP_2
    THEN
        IF IS_BIT_SET_L gSaveBools SBOOLS_bDrugBuyTutDone
        THEN            
            GET_LABEL_POINTER @TempMemory TEMP_4
            CALL @GetDrugName 1 TEMP_2 TEMP_5
            CALL @GetDrugPrice 1 TEMP_2 TEMP_6
            STRING_FORMAT TEMP_4 "%s~n~Price: $%d" TEMP_5 TEMP_6
            CALL_FUNCTION 0x588BE0 4 4 0 1 0 TEMP_4
        END
    END
    
    IF 0019:   VAR_4(TEMP_2,6i) > 0
    THEN
        0093: TEMP_4 = integer TEMP_7 to_float
        TEMP_4 *= 12.0
        TEMP_4 += 140.0
        
        SET_TEXT_FONT 1
        SET_TEXT_RIGHT_JUSTIFY TRUE
        SET_TEXT_EDGE ON 0 0 0 255
        
        GET_LABEL_POINTER @DRUG_NAME_QTY TEMP_5
        DISPLAY_TEXT_WITH_NUMBER 600.0 TEMP_4 TEMP_5(TEMP_2,8s) VAR_4(TEMP_2,6i)
        
        TEMP_7 += 1
    END
END

TEMP_2 = 0

FOR TEMP_3 = MIN_DRUG TO MAX_DRUG
    0085: TEMP_4 = VAR_4(TEMP_3,6i)
    IF TEMP_4 > 0
    THEN
        CALL @GetDrugPrice 1 TEMP_3 TEMP_5
        006A: TEMP_4 *= TEMP_5
        005A: TEMP_2 += TEMP_4
    END
END

0085: TEMP_3 = TEMP_7
0093: TEMP_3 = integer TEMP_3 to_float
TEMP_3 *= 12.0
TEMP_3 += 144.0

SET_TEXT_FONT 1
SET_TEXT_RIGHT_JUSTIFY TRUE
SET_TEXT_EDGE ON 0 0 0 255
DISPLAY_TEXT_WITH_NUMBER 600.0 TEMP_3 "_BMTOT" TEMP_2

IF IS_BUTTON_PRESSED PAD1 TRIANGLE
THEN VAR_3 = 1
ELSE
    IF VAR_3 == 1
    THEN
        VAR_3 = 0
        
        // Tell DEALER script to finish.
        CALL @CRunningScript__SetVar 3 TEMP_1 DEALER_VAR_DEAL2PLAYER 4
    END
END

IF IS_BUTTON_PRESSED PAD1 CROSS
THEN VAR_3 = 2
ELSE
    IF VAR_3 == 2
    THEN
        // Check we've actually got some drugs to buy and can afford em...
        TEMP_2 = 0
        TEMP_3 = 0
        FOR TEMP_4 = MIN_DRUG TO MAX_DRUG
            0085: TEMP_5 = VAR_4(TEMP_4,6i)
            IF TEMP_5 > 0
            THEN
                005A: TEMP_2 += TEMP_5
                CALL @GetDrugPrice 1 TEMP_4 TEMP_6
                006A: TEMP_6 *= TEMP_5
                005A: TEMP_3 += TEMP_6
            END
        END
        IF TEMP_2 > 0
        THEN
            0A8F: TEMP_4 = TEMP_3 - 1
            {IF IS_SCORE_GREATER 0 TEMP_4
            THEN}
            IF NOT IS_SCORE_GREATER player1 TEMP_4
            THEN
                STORE_SCORE player1 TEMP_4
                0062: TEMP_4 -= TEMP_3
                ABSI_L TEMP_4
                005A: gSaveDealerDebt += TEMP_4
                PRINT_FORMATTED_NOW "You now owe the dealer $%d." 4000 gSaveDealerDebt
            END
            CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_MONEY >> TEMP_5
            005A: TEMP_5 += TEMP_3
            CALL @CRunningScript__GetVar 2 TEMP_1 DEALER_VAR_CHAR >> TEMP_6
            SET_CHAR_MONEY TEMP_6 TEMP_5
            CALL @CRunningScript__SetVar 3 TEMP_1 DEALER_VAR_MONEY TEMP_5
            
            GET_VAR_POINTER VAR_4 TEMP_5
            CALL @StartTransaction 0 TEMP_6
            
            0085: TEMP_6(CTransaction_iCash,4s) = TEMP_3
            
            FOR TEMP_4 = MIN_DRUG TO MAX_DRUG
                0085: TEMP_5 = VAR_4(TEMP_4,6i)
                0A8E: TEMP_7 = TEMP_4 + CTransaction_DrugQty
                IF TEMP_5 > 0
                THEN
                    CALL @TakeDrugsFromDealerScript 3 TEMP_1 TEMP_4 TEMP_5
                    0085: TEMP_6(TEMP_7,4s) = TEMP_5
                ELSE
                    0006: TEMP_6(TEMP_7,4s) = 0
                END
                0006: VAR_4(TEMP_4,6i) = 0
            END
            
            CALL @CRunningScript__SetVar 3 TEMP_1 DEALER_VAR_DEAL2PLAYER 3
            {ELSE
                PRINT_STRING_NOW "You do not have enough cash." 4000
            END}
        ELSE
            PRINT_STRING_NOW  "You have not chosen any drugs." 4000
        END
        VAR_3 = 0
    END
END
RETURN

:DealerHook_End
GOSUB @EndProc
RET 0

{********************************************}

// Just avoid the fiends...

:FIENDS
HEX
0B00 01 @InitProc
END
{$I fiends.txt}
GOSUB @EndProc
RET 0

{********************************************}

// A pure (ish) script based on the original SA dealer script
{$I dealer.txt}

{********************************************}

// Final tasks...

:End
hex
0000 01 @InitProc
end
READ_MEMORY 0xB610E0 4 0 gDebugEndTime
RET 0

{********************************************}

:InitProc
WRITE_MEMORY gCurrentProcData 2 gNumProcVars 0
GET_VAR_POINTER 0@ gStartVarPointer
0085: SaveVar = gSaveDataPointer

IF 0019:   gNumProcVars > 0
THEN
    0085: TEMP_1 = gStackPointer
    READ_MEMORY 0xB7CB58 4 0 TEMP_2
    TEMP_2 *= 20.0
    0092: TEMP_2 = float TEMP_2 to_integer
    
    0085: TEMP_3 = gStartVarPointer
    0085: TEMP_3(-32@,4s) = TEMP_1(TEMP_1,4s)
    005A: TEMP_3(-32@,4s) += TEMP_2
    0085: TEMP_3(-33@,4s) = TEMP_1(-1@,4s)
    005A: TEMP_3(-33@,4s) += TEMP_2
    TEMP_1 += 8
    
    0A8E: TEMP_2 = gNumProcVars + VAR_START_NUM

    FOR TEMP_4 = VAR_START_NUM TO TEMP_2
        0085: TEMP_3(TEMP_4,4s) = TEMP_1(TEMP_1,4s)
        TEMP_1 += 4
    END
END
RETURN

:ResetVars
0A8E: TEMP_1 = gNumProcVars + VAR_START_NUM
0085: TEMP_2 = gStartVarPointer
0006: TEMP_2(-32@,4s) = 0
0006: TEMP_2(-33@,4s) = 0
FOR TEMP_3 = VAR_START_NUM TO TEMP_1
    0006: TEMP_2(TEMP_3,4s) = 0
END
RETURN

:EndProc
0085: TEMP_3 = gStartVarPointer
0085: TEMP_4 = gStackPointer
0085: TEMP_4(TEMP_4,4s) = TEMP_3(-32@,4s)
0085: TEMP_4(-1@,4s) = TEMP_3(-33@,4s)
TEMP_4 += 8
0A8E: TEMP_2 = gNumProcVars + VAR_START_NUM
FOR TEMP_5 = VAR_START_NUM TO TEMP_2
    0085: TEMP_4(TEMP_4,4s) = TEMP_3(TEMP_5,4s)
    TEMP_4 += 4
END
0085: gStackPointer = TEMP_4
RETURN

:GetSaveVarIndex
GOSUB @GetGlobalVarIndex
0085: SaveVar = gSaveDataPointer
RETURN

:GetGlobalVarIndex
SET_BIT_L 38@ 8
SVar = @GLOBAL_VARIABLES_INDEX
0085: SVar = SVar(SVar,4s)
RETURN

{********************************************}

// Objective-SCM!
PRINTSTRING "Objective-SCM"
{$I cllib.inc}

{********************************************}

// Doesn't work :(
// We also need a way to draw textures with no conflicts...
//:DrawSprite{\__(pTexture,_fX,_fY,_fSizeX,_fSizeY,_fAngle,_dwRGBA)__}
{READ_MEMORY 0xA44B5C 2 OFF 11@
0A90: 10@ = 11@ * 60
10@ += 0xA92D68
IF 6@ == 0
THEN 0006: 10@(10@,4s) = 4
ELSE 0006: 10@(10@,4s) = 5
END
3@ *= 0.5
4@ *= 0.5
0087: 10@(-2@,4s) = 1@
0087: 10@(-4@,4s) = 1@
0087: 10@(-3@,4s) = 2@
0087: 10@(-5@,4s) = 2@
0063: 10@(-2@,4s) -= 3@
0063: 10@(-3@,4s) -= 4@
005B: 10@(-3@,4s) += 4@
005B: 10@(-5@,4s) += 4@
0087: 10@(-6@,4s) = 5@
IF NOT 6@ == 0
THEN
    0013: 6@ *= 0.0174532925
    0087: 10@(-7@,4s) = 6@
ELSE
    0007: 10@(-7@,4s) = 0
END
0085: 10@(-8@,4s) = 0@
CALL @GetScreenRealCoordsX 1 10@(-2@,4s) 12@
CALL @GetScreenRealCoordsX 1 10@(-4@,4s) 13@
CALL @GetScreenRealCoordsY 1 10@(-3@,4s) 14@
CALL @GetScreenRealCoordsY 1 10@(-5@,4s) 15@
11@ += 1
WRITE_MEMORY 0xA44B5C 2 OFF 11@
RET 0

:_DrawSprite_ASM
HEX
8b 46 20                   //mov eax,[esi+20]
85 c0                      //test eax,eax
75 09                      //jne 09
8d 0c 8d 68 4b a9 00       //lea ecx,[ecx*4+00a94b68]
eb 03                      //jmp 03
8d 4e 20                   //lea ecx,[esi+20]
8b 06                      //mov eax,[esi]
83 f8 05                   //cmp eax,05
74 07                      //je 07
b8 81 4a 46 00             //mov eax,00464a81
ff e0                      //jmp eax
b8 74 4b 46 00             //mov eax,00464b74
ff e0                      //jmp eax
END

:GetScreenRealCoordsX
READ_MEMORY 0xC17044 4 FALSE 1@
0017: 1@ /= 640.0
006B: 0@ *= 1@
RET 1 0@

:GetScreenRealCoordsY
READ_MEMORY 0xC17048 4 FALSE 1@
0017: 1@ /= 448.0
006B: 0@ *= 1@
RET 1 0@}

{********************************************}

:ClearLog
IF OPEN_FILE "cleo\BlackMarket\log.txt" "w" 0@
THEN
    CLOSE_FILE 0@
END
RET 0

:Log
RET 0
IF OPEN_FILE "cleo\BlackMarket\log.txt" "a+" 0@
THEN
    GET_LABEL_POINTER @TempMemory 1@
    WRITE_STRING_TO_FILE 0@ 1@
    WRITE_FORMATTED_STRING_TO_FILE 0@ "%c%c" 0xD 0xA
    CLOSE_FILE 0@
END
RET 0

:Error
IF OPEN_FILE "cleo\BlackMarket\dump.txt" "wt" 0@
THEN
    GET_LABEL_POINTER @TempMemory 1@
    WRITE_STRING_TO_FILE 0@ 1@
    CLOSE_FILE 0@
    WHILE IS_HELP_MESSAGE_BEING_DISPLAYED
        WAIT 0
    END
    PRINT_HELP_STRING "Black Market: Error encountered"
ELSE
    WHILE IS_HELP_MESSAGE_BEING_DISPLAYED
        WAIT 0
    END
    PRINT_HELP_STRING "Black Market: Error encountered - failed to dump error report"
END
RET 0

:Push{\__(Val)__}
0085: 1@ = 0@
GOSUB @GetGlobalVarIndex
0085: 2@ = gStackPointer
0085: 2@(2@,4s) = 1@
000A: gStackPointer += 4
RET 0

:Pop{\__[Val]__}
GOSUB @GetGlobalVarIndex
000E: gStackPointer -= 4
0085: 0@ = gStackPointer
RET 1 0@(0@,4s)

:UnpackDWORD{\__[values...]__(dword)__}
BITWISE_AND 0@ AND 0xFF STORE_TO 4@
BITWISE_AND 0@ AND 0xFF00 STORE_TO 5@
BITWISE_AND 0@ AND 0xFF0000 STORE_TO 6@
BITWISE_AND 0@ AND 0xFF000000 STORE_TO 7@
BITWISE_SHR 5@ SHR 0x8 STORE_TO 5@
BITWISE_SHR 6@ SHR 0x10 STORE_TO 6@
BITWISE_SHR 7@ SHR 0x18 STORE_TO 7@
RET 4 4@ 5@ 6@ 7@

:PackDWORD{\__[dword]_(values...)__}
BITWISE_SHL 1@ SHL 0x8 STORE_TO 1@
BITWISE_SHL 2@ SHL 0x10 STORE_TO 2@
BITWISE_SHL 3@ SHL 0x18 STORE_TO 3@
BITWISE_AND 0@ AND 0xFF STORE_TO 0@
BITWISE_AND 1@ AND 0xFF00 STORE_TO 1@
BITWISE_AND 2@ AND 0xFF0000 STORE_TO 2@
BITWISE_AND 3@ AND 0xFF000000 STORE_TO 3@
BITWISE_OR 5@ OR 0@ STORE_TO 5@
BITWISE_OR 5@ OR 1@ STORE_TO 5@
BITWISE_OR 5@ OR 2@ STORE_TO 5@
BITWISE_OR 5@ OR 3@ STORE_TO 5@
RET 1 5@

{}:DisplayText_MEM
0000:
0000:
0000:
0000:

:DisplayText{\__(x,_y,_pStr)__}
NOT 2@ == 0
ELSE_GOTO @DisplayText_END
0085: 3@ = 0@
0085: 4@ = 1@
GOSUB @GetGlobalVarIndex
31@ = @DisplayText_MEM
803C:   NOT gNumCycles == 31@(31@,4s)
ELSE_GOTO @DisplayText_1
0085: 31@(31@,4s) = gNumCycles
0006: 31@(-1@,4s) = 0
{}:DisplayText_1
STRING_FORMAT 5@v "__TMP%X" 31@(-1@,4s)
ADD_TEXT_LABEL 5@v 2@
DISPLAY_TEXT 3@ 4@ 5@v
31@(-1@,4s) += 1
{}:DisplayText_END
RET 0

:GenerateDrugPrices
1@ = FPRICE_WEED
2@ = FPRICE_COKE
3@ = FPRICE_HEROIN
4@ = FPRICE_ECSTASY
5@ = FPRICE_STEROID
6@ = FPRICE_SHROOM
FOR 7@ = MIN_DRUG TO MAX_DRUG
    CALL @GenerateDrugPriceMultiplier 0 0@
    006B: 1@(7@,MAX_DRUGf) *= 0@
    0092: 1@(7@,MAX_DRUGi) = float_to_int 1@(7@,MAX_DRUGf)
    CALL @SetDrugPrice 2 7@ 1@(7@,6i)
END
RET 0

:GenerateDrugValues
1@ = FPRICE_WEED
2@ = FPRICE_COKE
3@ = FPRICE_HEROIN
4@ = FPRICE_ECSTASY
5@ = FPRICE_STEROID
6@ = FPRICE_SHROOM
FOR 7@ = MIN_DRUG TO MAX_DRUG
    CALL @GenerateDrugValueMultiplier 0 0@
    006B: 1@(7@,MAX_DRUGf) *= 0@
    0092: 1@(7@,MAX_DRUGi) = float_to_int 1@(7@,MAX_DRUGf)
    CALL @SetDrugValue 2 7@ 1@(7@,MAX_DRUGi)
END
RET 0

:GetDrugPrice
1@ = @DRUG_PRICES
RET 1 1@(0@,4s)

:GetDrugValue
1@ = @DRUG_VALUES
RET 1 1@(0@,4s)

:SetDrugPrice
2@ = @DRUG_PRICES
0085: 2@(0@,4s) = 1@
RET 0

:SetDrugValue
2@ = @DRUG_VALUES
0085: 2@(0@,4s) = 1@
RET 0

:GetDrugName
1@ = @DRUG_NAMES
GET_VAR_POINTER 1@(0@,10s) 2@
RET 1 2@

:GetReputation
GOSUB @GetSaveVarIndex
RET 1 gSaveStatReputation

:GenerateDrugPriceMultiplier
0@ = 1.0 
GET_TOTAL_RESPECT 1@
IF 1@ > 7
THEN
    1@ = 7
ELSE
    IF 1@ < 0
    THEN 1@ = 0
    END
END

0093: 1@ = integer 1@ to_float
1@ /= 7.0
1@ -= 0.5
1@ *= 0.2
0063: 0@ -= 1@

CALL @GetReputation 0 1@
1@ /= 1000.0
1@ -= 0.75
1@ *= 0.8
0063: 0@ -= 1@

GENERATE_RANDOM_FLOAT 1@
1@ -= 0.5
1@ /= 5.0
0063: 0@ -= 1@

IF 0@ < 0.0
THEN
    0@ = 0.1
END
RET 1 0@

:GenerateDrugValueMultiplier
0@ = 1.0
GET_TOTAL_RESPECT 1@
IF 1@ > 7
THEN
    1@ = 7
ELSE
    IF 1@ < 0
    THEN 1@ = 0
    END
END

0093: 1@ = int_to_float 1@
1@ /= 7.0
1@ -= 0.5
1@ *= 0.2
005B: 0@ += 1@

CALL @GetReputation 0 1@
1@ /= 1000.0
1@ -= 0.4
1@ *= 0.8
005B: 0@ += 1@

GENERATE_RANDOM_FLOAT 1@
1@ -= 0.5
1@ /= 5.0
005B: 0@ += 1@

IF 0@ < 0.0
THEN
    0@ = 0.1
END
RET 1 0@

:GetSupplyDemandRatio
GOSUB @GetSaveVarIndex
0087: 3@ = gSaveDemand
0073: 3@ /= gSaveSupply
IF 3@ > 2.0
THEN 3@ = 2.0
ELSE
    IF 3@ < 0.5
    THEN 3@ = 0.5
    END
END
RET 1 3@

{}:Transaction_MEM
0000:
0000:
0000:
0000:

:StartTransaction{\__[pTransactionData]__}
0A90: 0@ = MAX_DRUG * 4
0@ += CTransaction_SIZE
2@ = @Transaction_MEM
IF NOT ALLOCATE_MEMORY 0@ 1@
THEN
    GET_LABEL_POINTER @TempMemory 1@
    WRITE_MEMORY 1@ 0@ 0 0
    1@ = @TempMemory
ELSE
    WRITE_MEMORY 1@ 0@ 0 0
END
GET_GAME_TIMER 1@(CTransaction_iTime,4s)
0085: 2@(2@,4s) = 1@
RET 1 1@

{:AnimateTransaction
0@ = @Transaction_MEM
0085: 0@ = 0@(0@,4s)
RET 0}

:FinishTransaction
GOSUB @GetSaveVarIndex
5@ = @Transaction_MEM
0085: 1@ = 5@(5@,4s)
IF NOT 1@ == 0
THEN 
    0093: 2@ = int_to_float 1@(CTransaction_iCash,4s)
    INCREMENT_FLOAT_STAT STAT_DRUGS_BUDGET 2@
    0A90: 2@ = 1@(CTransaction_iCash,4s) * -1
    0062: gSaveDrugProfit -= 1@(CTransaction_iCash,4s)
    //005A: gSaveDrugLoss += 1@(CTransaction_iCash,4s)
    ADD_SCORE player1 2@
    GET_VAR_POINTER gSaveDrugQtyBegin 3@
    7@ = 0
    FOR 4@ = MIN_DRUG TO MAX_DRUG
        0A8E: 6@ = 4@ + CTransaction_DrugQty
        005A: 7@ += 1@(6@,4s)
        PRINTINT2 "BOUGHT" 1@(6@,4s)
        005A: 3@(4@,4s) += 1@(6@,4s)
    END
    INCREMENT_INT_STAT STAT_DRUGS_BOUGHT 7@
    000A: gSaveNumDealsDone += 1
    IF 1@ > 0
    THEN FREE_MEMORY 1@
    END
    0006: 5@(5@,4s) = 0
END
RET 0

:SetIsDealGoingOn{\__(bOn)__}
0085: 1@ = 0@
GOSUB @GetGlobalVarIndex
0085: gDealGoingOn = 1@
RET 0

:IsDealGoingOn
GOSUB @GetGlobalVarIndex
8039:   NOT gDealGoingOn == 0
RET 0

:IsDealNotGoingOn
GOSUB @GetGlobalVarIndex
0039:   gDealGoingOn == 0
RET 0

:GetIsDealGoingOn{\__[bResult]__}
GOSUB @GetGlobalVarIndex
RET 1 gDealGoingOn

:GetTotalSafeDrugQuantity{\__[nQty]__}
GOSUB @GetSaveVarIndex
GET_VAR_POINTER gSaveSafeDrugQtyBegin 1@
FOR 2@ = MIN_DRUG TO MAX_DRUG
    005A: 3@ += 1@(2@,4s)
END
RET 1 3@

:GiveDrugs{\__(nDrug,_nQty)__}
CALL @GetDrugQuantity 1 0@ 2@
005A: 2@ += 1@
CALL @SetDrugQuantity 2 0@ 2@
RET 0

:TakeDrugs{\__(nDrug,_nQty)__}
CALL @GetDrugQuantity 1 0@ 2@
0062: 2@ -= 1@
CALL @SetDrugQuantity 2 0@ 2@
RET 0

:GetDrugQuantity{\__[nQty]__(nDrug)__}
IF AND
    0@ <= MAX_DRUG
    0@ >= MIN_DRUG
THEN
    0085: 1@ = 0@
    GOSUB @GetSaveVarIndex
    GET_VAR_POINTER gSaveDrugQtyBegin 2@
    RET 1 2@(1@,4s)
END
RET 1 -1

:SetDrugQuantity{\__(nDrug,_nQty)__}
IF AND
    0@ <= MAX_DRUG
    0@ >= MIN_DRUG
THEN
    0085: 2@ = 0@
    GOSUB @GetSaveVarIndex
    GET_VAR_POINTER gSaveDrugQtyBegin 3@
    0085: 3@(2@,4s) = 1@
END
RET 0

:GetTotalDrugQuantity{\__[nQty]__}
GOSUB @GetSaveVarIndex
GET_VAR_POINTER gSaveDrugQtyBegin 1@
FOR 2@ = MIN_DRUG TO MAX_DRUG
    005A: 3@ += 1@(2@,4s)
END
RET 1 3@

:GiveDrugsToDealerScript{\__(pScript,_nDrug,_nQty)__}
CALL @GetDealerScriptDrugQuantity 2 0@ 1@ 3@
005A: 3@ += 2@
CALL @SetDealerScriptDrugQuantity 3 0@ 1@ 3@
RET 0

:TakeDrugsFromDealerScript{\__(pScript,_nDrug,_nQty)__}
CALL @GetDealerScriptDrugQuantity 2 0@ 1@ 3@
0062: 3@ -= 2@
IF 3@ < 0
THEN 3@ = 0
END
CALL @SetDealerScriptDrugQuantity 3 0@ 1@ 3@
RET 0

:SetDealerScriptDrugQuantity{\__(pScript,_nDrug,_nQty)__}
1@ += DEALER_VAR_NUMWEED
IF 2@ >= 10
THEN 2@ = 9
END
CALL @CRunningScript__SetVar 3 0@ 1@ 2@
RET 0

:GetDealerScriptDrugQuantity{\__(pScript,_nDrug)__}
1@ += DEALER_VAR_NUMWEED
CALL @CRunningScript__GetVar 2 0@ 1@ 2@
RET 1 2@

:GetDealerScriptTotalDrugQuantity{\__(pScript)__}
3@ = 0
FOR 1@ = MIN_DRUG TO MAX_DRUG
    CALL @GetDealerScriptDrugQuantity 2 0@ 1@ 2@
    005A: 3@ += 2@
END
RET 1 3@

:AddAmbientSmoke{\__(fAmount)__}
0085: 1@ = 0@
GOSUB @GetGlobalVarIndex
005B: gSmokeBlown += 1@
GET_CHAR_COORDINATES scplayer gSmokeBlownX gSmokeBlownY gSmokeBlownZ
RET 0

:HasOverdosed{\__[bool]__}
GOSUB @GetGlobalVarIndex
8039:   NOT gHasOverdosed == 0
RET 0

:ClearAllDrugLevels
0006: gWeedLevel = 0
0006: gWeedLevelInSystem = 0
0006: gTotalDrugLevel = 0
0006: gDrugLevelInSystem = 0
RET 0

:IncDrugTakenStat{\__(nDrug)__}
0085: 1@ = 0@
GOSUB @GetSaveVarIndex
GET_VAR_POINTER gSaveAbuseStatsBegin 2@
000A: 2@(1@,4s) += 1
RET 0

:IncDrugLevel{\__(nDrug,_nBy)__}
0085: 2@ = 0@
GOSUB @GetGlobalVarIndex
IF 2@ == WEED
THEN
    005A: gWeedLevel += 1@
    005A: gTotalDrugLevel += 1@
    GET_GAME_TIMER gWeedTimeLastTook
END
RET 0

:DecDrugLevel{\__(nDrug,_nBy)__}
0085: 2@ = 0@
GOSUB @GetGlobalVarIndex
IF 2@ == WEED
THEN
    0062: gWeedLevel -= 1@
    0062: gTotalDrugLevel -= 1@
    IF 8029:   NOT gWeedLevel >= 0
    THEN 0006: gWeedLevel = 0
    END
END
RET 0

:IncDrugLevelInSystem{\__(nDrug,_nBy)__}
0085: 2@ = 0@
GOSUB @GetGlobalVarIndex
IF 2@ == WEED
THEN
    005A: gWeedLevelInSystem += 1@
    005A: gDrugLevelInSystem += 1@
END
RET 0

:DecDrugLevelInSystem{\__(nDrug,_nBy)__}
0085: 2@ = 0@
GOSUB @GetGlobalVarIndex
IF 2@ == WEED
THEN
    0062: gWeedLevelInSystem -= 1@
    0062: gDrugLevelInSystem -= 1@
    IF 8029:   NOT gWeedLevelInSystem >= 0
    THEN 0006: gWeedLevelInSystem = 0
    END
END
IF 8029:   NOT gDrugLevelInSystem >= 0
THEN 0006: gDrugLevelInSystem = 0
END
RET 0

:IncNumHomiesDealt
GOSUB @GetSaveVarIndex
000A: gSaveNumHomiesDealt += 1
GET_CHAR_COORDINATES scplayer 2@ 3@ 4@
GET_TOWN_AT_COORDS 2@ 3@ 4@ STORE_TO 5@
IF 5@ == TOWN_LS
THEN
    GET_NAME_OF_ZONE 2@ 3@ 4@ 6@s
    GET_ZONE_GANG_DENSITY 6@s GANG_GROVE 8@
    8@ -= 2
    SET_ZONE_GANG_DENSITY 6@s GANG_GROVE 8@
    
    // Check for existing enemy gang population
    GET_ZONE_GANG_DENSITY 6@s GANG_BALLAS 8@
    
    IF NOT 8@ > 0
    THEN
        GET_ZONE_GANG_DENSITY 6@s GANG_VAGOS 8@
        IF NOT 8@ > 0
        THEN
            // No existing gangs? Lets reintroduce them...
            GENERATE_RANDOM_INT_IN_RANGE 0 3 STORE_TO 7@
            IF 7@ == GANG_GROVE
            THEN
                7@ = GANG_BALLAS
            END
            GET_ZONE_GANG_DENSITY 6@s 7@ 8@
        ELSE
            7@ = GANG_VAGOS
        END
    ELSE
        7@ = GANG_BALLAS
    END
    
    8@ += 2
    SET_ZONE_GANG_DENSITY 6@s 7@ 8@
    READ_MEMORY 0x96AB90 1 FALSE 9@
    
    IF 9@ == 0
    THEN CALL_FUNCTION 0x572440 1 1 1
    ELSE CALL_FUNCTION 0x572440 1 1 0
    END
END
RET 0

:GetPedCash{\__[nMoney]_(hPed)__}
GET_CHAR_POINTER 0@ 1@
1@ += 0x756
READ_MEMORY 1@ 4 OFF 2@
RET 1 2@

:IsPedOnDrugs{\__[bool]_(hPed)__}
GET_CHAR_POINTER 0@ 1@
1@ += 0x478
READ_MEMORY 1@ 2 OFF 2@
BITWISE_AND 2@ 0x400 2@
NOT 2@ == 0
RET 0

// Lots of stat-related stuff...

:Stats_Init
GOSUB @GetSaveVarIndex
0006: gSaveStatEpisode = 1
0006: gSaveStatWeedTaken = 0
0006: gSaveStatCokeTaken = 0
0006: gSaveStatHeroinTaken = 0
0006: gSaveStatEcstasyTaken = 0
0006: gSaveStatSteroidsTaken = 0
0006: gSaveStatShroomsTaken = 0
0007: gSaveStatReputation = 100.0
0007: gSaveStatDrugExperience = 50.0
RET 0

:Stat_DisplayUpdateBar{\__(bDir,_wStatId,_fVal)__}
READ_MEMORY 0xB794D4 1 FALSE 3@
READ_MEMORY 0x8CDE56 1 FALSE 4@
IF AND
    3@ == 0
    NOT 4@ == 0
THEN
    READ_MEMORY 0xB7356F 1 FALSE 3@
    READ_MEMORY 0xB6F065 1 FALSE 4@
    READ_MEMORY 0xBA82E3 1 FALSE 5@
    IF AND
        3@ == 0
        4@ == 0
        5@ == 0
    THEN
        IF NOT IS_HELP_MESSAGE_BEING_DISPLAYED
        THEN
            CALL_FUNCTION 0x588D40 4 4 1000.0 2@ 1@ 0@
            RET 0
        END
    END
END
WRITE_MEMORY 0xB794D4 1 0 FALSE
RET 0

:Stat_IncRep{\__(nPoints)__}
IF 0@ <= 0
THEN RET 0
END
0093: 1@ = int_to_float 0@
1@ /= 1000000.0
GOSUB @GetSaveVarIndex
005B: gSaveStatReputation += 1@
CALL @Stat_DisplayUpdateBar 3 1 825 1@
RET 0

:Stat_DecRep{\__(nPoints)__}
IF 0@ <= 0
THEN RET 0
END
0093: 1@ = int_to_float 0@
1@ /= 1000000.0
GOSUB @GetSaveVarIndex
0063: gSaveStatReputation -= 1@
CALL @Stat_DisplayUpdateBar 3 0 825 1@
RET 0

:Stat_IncDrugExp{\__(nPoints)__}
IF 0@ <= 0
THEN RET 0
END
0093: 1@ = int_to_float 0@
1@ *= 2.5
GOSUB @GetSaveVarIndex
005B: gSaveStatDrugExperience += 1@
CALL @Stat_DisplayUpdateBar 3 1 827 1@
RET 0

:Stats_OnDrugSold{\__(nDrug,_nNum)__}
0085: 2@ = 0@
IF 1@ <= 0
THEN 3@ = 1
ELSE 0085: 3@ = 1@
END

GET_INT_STAT STAT_DRUGS_SOLD 4@

MODULO 4@ 3 5@

IF 5@ == 0
THEN
    READ_MEMORY 0x8CDE56 1 FALSE 7@
    SHOW_UPDATE_STATS FALSE
    6@ = 1
ELSE
    6@ = 0
END

INCREMENT_INT_STAT STAT_DRUGS_SOLD 1

IF 0@ == WEED
THEN CALL @Stat_IncRep 1 10
ELSE
    IF 0@ == COKE
    THEN CALL @Stat_IncRep 1 20
    ELSE
        IF 0@ == HEROIN
        THEN CALL @Stat_IncRep 1 15
        ELSE
            CALL @Stat_IncRep 1 5
        END
    END
END

IF 6@ == 1
THEN
    SHOW_UPDATE_STATS 7@
END
RET 0

:GetRandomDrugForPed{\__[nDrug]_(hPed)__}
CALL @GetDrugsMaskForPed 1 0@ 1@
BITWISE_AND 1@ DRUG_FLAG_WEED 2@
BITWISE_AND 1@ DRUG_FLAG_COKE 3@
BITWISE_AND 1@ DRUG_FLAG_HEROIN 4@
BITWISE_AND 1@ DRUG_FLAG_ECSTASY 5@
BITWISE_AND 1@ DRUG_FLAG_STEROID 6@
BITWISE_AND 1@ DRUG_FLAG_SHROOM 7@
GENERATE_RANDOM_INT_IN_RANGE MIN_DRUG MAX_DRUG 8@
FOR 9@ = MIN_DRUG TO MAX_DRUG
    IF 2@(8@,MAX_DRUGi) > 0
    THEN RET 1 8@
    END
    8@ += 1
    IF 8@ > MAX_DRUG
    THEN 8@ = MIN_DRUG
    END
END
RETURN_FALSE
RET 1 0

:GetDrugsMaskForPed{\__[bDrugsMask]_(hPed)__}
GET_CHAR_MODEL 0@ 1@
GOSUB @GetGlobalVarIndex
0085: 2@ = gNumPedDrugInfoEntries
0085: 3@ = gPedDrugInfo
FOR 4@ = 0 TO 2@
    READ_MEMORY 3@ 2 OFF 5@
    IF 003B:   5@ == 1@
    THEN
        3@ += 2
        READ_MEMORY 3@ 1 OFF 5@
        RET 1 5@
    END
    3@ += 3
END
RETURN_FALSE
RET 1 0

:IsDebug
GOSUB @GetGlobalVarIndex
0039:   gDebugModeOn == 1
RET 0

:strncmp{\__[bool]__(str1,_str2,_size)__}
GET_LABEL_POINTER @FUNC_stncmp 3@
CALL_FUNCTION_RETURN 3@ 3 3 2@ 1@ 0@ 4@
4@ == 0
RET 0

{}:FUNC_stncmp
HEX
55                         // push ebp
8B Ec                      // mov ebp,esp
57                         // push edi
56                         // push esi
53                         // push ebx
8B 4D 10                   // mov ecx,[ebp+10]
E3 27                      // jecxz 00821502
8B D9                      // mov ebx,ecx
8B 7D 08                   // mov edi,[ebp+08]
8B F7                      // mov esi,edi
33 C0                      // xor eax,eax
F2 AE                      // repne scasb
F7 D9                      // neg ecx
03 CB                      // add ecx,ebx
8B FE                      // mov edi,esi
8B 75 0C                   // mov esi,[ebp+0c]
F3 A6                      // repe cmpsb
8A 46 FF                   // mov al,[esi-01]
33 C9                      // xor ecx,ecx
3A 47 FF                   // cmp al,[edi-01]
77 05                      // ja 00821500
74 05                      // je 00821502
83 E9 02                   // sub ecx,02
F7 D1                      // not ecx
8B C1                      // mov eax,ecx
5B                         // pop ebx
5E                         // pop esi
5F                         // pop edi
C9                         // leave
C3                         // ret
END

:GetModelInfo
1@ = 0xA9B0C8
RET 1 1@(0@,4s)

{}:CTheScripts__GetNextActiveScript_MEM
0000:
0000:
0000:
0000:

:CTheScripts__GetNextActiveScript{\__[pScript]_(bStartNewSearch)__}
31@ = @CTheScripts__GetNextActiveScript_MEM
IF 0@ == 0
THEN
    NOT 31@(31@,4s) == 0
    ELSE_GOTO @CTheScripts__GetNextActiveScript_END
    0085: 1@ = 31@(31@,4s)
ELSE
    1@ = 0xA8B42C
    0085: 1@ = 1@(1@,4s)
END
0085: 31@(31@,4s) = 1@(1@,4s)
NOT 1@(1@,4s) == 0
{}:CTheScripts__GetNextActiveScript_END
RET 1 1@

:CTheScripts__GetNextActiveScriptNamed{\__[pScript]_(bStartNewSearch,_pName)__}
REPEAT
    CALL @CTheScripts__GetNextActiveScript 1 0@ 2@
    ELSE_GOTO BREAK
    NOT 2@ == 0
    ELSE_GOTO BREAK
    0@ = 0
    0A8E: 3@ = 2@ + 8
UNTIL CALL @strncmp 3 1@ 3@ 8
RET 1 2@

:CRunningScript__IsScriptBrain{\__[bool]_(pScript)__}
0@ += 0xC7
READ_MEMORY 0@ 1 0 0@
NOT 0@ == 0
RET 0

:CRunningScript__GetVar{\__[iValue]_(pScript,iVarNum)__}
0@ += 0x3C
RET 1 0@(1@,4s)

:CRunningScript__SetVar{\__(pScript,iVarNum,iValue)__}
0@ += 0x3C
0085: 0@(1@,4s) = 2@
RET 0

{********************************************}

// .data

{}:DRUG_PRICES
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:

{}:DRUG_VALUES
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:
0000:

{}:DRUG_NAMES
HEX
// Aliases?
{"Gringo"   00000000
"Whities"    000000
"Brownies"     0000
"Chirps"   00000000
"Ronnies"    000000
"Shrewds"    000000}
"Weed" 000000000000
"Coke" 000000000000
"Heroin"   00000000
"Ecstasy"    000000
"Steroids"     0000
"Shrooms"    000000
END

{}:DRUG_NAME_QTY
HEX
"_BMDQ0" 0000
"_BMDQ1" 0000
"_BMDQ2" 0000
"_BMDQ3" 0000
"_BMDQ4" 0000
"_BMDQ5" 0000

// Stat format
"_BMS%d" 0000
END

// Extend stats limit
{}:_STATGXT
HEX
81 FF 20 03 00 00           // [0]  cmp edi, 800
7D 0C                       // [6]  jge +C
68 F0 42 86 00              // [8]  push "STAT%d"
B8 &0x55A83E                // [13] mov eax, 0x55A83E
FF E0                       // [18] jmp eax
68 F0 42 86 00              // [20] push "STAT%d"   // "_BMS%d"
B8 &0x55A83E                // [25] mov eax, 0x55A83E
FF E0                       // [30] jmp eax
END

// Allow stats to appear in update messages
{}:_STATDISP
HEX
81 F9 20 03 00 00           // [0]  cmp ecx, 800
7D 0C                       // [6]  jge +C
68 &0x8642F0                // [8]  push "STAT%d"
B8 &0x58BC25                // [13] mov eax, 0x58BC25
FF E0                       // [18] jmp eax
68 &0x8642F0                // [20] push "STAT%d"   // "_BMS%d"
B8 &0x58BC25                // [25] mov eax, 0x58BC25
FF E0                       // [30] jmp eax
END

{}:_STATARRAY
HEX
66 3D 20 03                 // [0]  cmp ax, 800
0F B7 C8                    // [4]  movzx ecx,ax
B8 0C A8 55 00              // [7]  mov eax, 0x55A80C
7D 09                       // [12] jge +9
DB 04 8D 20 8E B7 00        // [14] fild dword ptr [ecx*4+0xB78E20]
FF E0                       // [21] jmp eax
81 E9 20 03 00 00           // [23] sub ecx, 800
DB 04 8D 20 8E B7 00        // [29] fild dword ptr [ecx*4+0xB78E20]
FF E0                       // [36] jmp eax
END

{}:_GETSTAT
HEX
{66 3D 20 03                 // [0]  cmp ax, 800
0F B7 C8                    // [4]  movzx ecx,ax
7D 08                       // [7]  jge +8
DB 04 8d 20 8E B7 00        // [9]  fild dword ptr [ecx*4+0xB78E20]
C3                          // [16] ret}
0F B7 C8                    // [0]  movzx ecx,ax
81 E9 20 03 00 00           // [3]  sub ecx, 800
66 83 F9 19                 // [9]  cmp cx, 25
7D 08                       // [13] jge +8
DB 04 8D 20 8E B7 00        // [15] fild dword ptr [ecx*4+0xB78E20]
C3                          // [22] ret
D9 04 8D 20 8E B7 00        // [23] fld dword ptr [ecx*4+0xB78E20]
C3                          // [30] ret
END

{}:_SPECIALSTAT
HEX
3D E0 01 00 00              // [0]  cmp eax,0x1E0
73 07                       // [5]  jae +7
B8 A6 B5 55 00              // [7]  mov eax,0x55B5A6
FF E0                       // [12] jmp eax
2D E0 01 00 00              // [14] sub eax,0x1E0
83 F8 32                    // [19] cmp eax,0x32
77 0E                       // [22] ja +14
0F B6 88 00 00 00 00        // [24] movzx ecx,byte ptr [eax+@_SPECIALSTAT_TT]
FF 24 8D 00 00 00 00        // [31] jmp dword ptr [ecx*4+@_SPECIALSTAT_JT]
B8 A6 B5 55 00              // [38] mov eax,0x55B5A6
FF E0                       // [43] jmp eax
END

{}:_SPECIALSTAT_TT
HEX
00 00 00 00 00 00 00 00 00 00   // 800-809
00 00 00 00 00 00 00 00 00 00   // 810-819
00 00 00 00 00 00 00 00 00 00   // 820-829
00 00 00 00 00 00 00 00 00 00   // 830-839
00 00 00 00 00 00 00 00 00 00   // 840-849
01 // 850...
END

{}:_SPECIALSTAT_JT
HEX
@_SPECIALSTAT_JDEF
@_SPECIALSTAT_J850
END

PRINTSTRING '_BMS850'

:_SPECIALSTAT_J850
HEX
3B F5                       // [0]  cmp esi, ebp
74 07                       // [2]  jz +7
B8 &0x55B522                // [4]  mov eax, 0x55B522
FF E0                       // [9]  jmp eax
//////////////////////////////
B8 &0x55B626                // [11] mov eax, 0x55B626
FF E0                       // [16] jmp eax
END

{}:_SPECIALSTAT_JDEF    // (default)
HEX
B8 &0x55B5A6                // [0]  mov eax, 0x55B5A6
FF E0                       // [5]  jmp eax
END

// Menu stats screen
{}:_STATMAX
0000:
0000:

{}:_STATDATA
HEX

// Overall
5203 0A 01 01 00    // Mod Version
FFFF 00 01 00 00    // DUMMY    
3903 0A 01 00 00    // Reputation

// Finances
DA00 00 01 00 00    // Drugs Bought
D900 00 01 00 00    // Drugs Sold
3D00 03 01 00 00    // Drug Budget
2103 03 01 00 00    // Drug Profit
//2303 03 01 00 00    // Drug Loss
2003 03 00 00 00    // Money Earned
2203 03 00 00 00    // Dealer Debt

// Abuse
3B03 0A 01 00 00    // Drug Experience
2403 00 01 00 00    // Weed
2503 00 01 00 00    // Coke
2603 00 01 00 00    // Heroin
2703 00 01 00 00    // Ecstasy
2803 00 01 00 00    // Steroids
2903 00 01 00 00    // Shrooms

9DFF 00 00 00 00
END

:_STATGETASM
HEX
BB 00 00 00 00      // mov ebx, (_STATDATA)
B8 &0x55A7D0        // mov eax, (return)
FF E0               // jmp eax
END

:_STATGETINTASM
HEX
66 83 FF 52                 // cmp di, 0x52
73 07                       // jae +7
B8 &0x55AABC                // mov eax, 0x55AABC
FF E0                       // jmp eax
66 81 FF 20 03              // cmp di, 0x320
72 17                       // jb +23
0F B7 CF                    // movzx ecx, di
81 E9 20 03 00 00           // sub ecx, 0x320
DB 04 8D &0xB78E20          // fild dword ptr [ecx*4+(_STATDATA)]
B8 &0x55AAC6                // mov eax, 0x55AAC6
FF E0                       // jmp eax
B8 &0x55B5E9                // mov eax, 0x55B5E9
FF E0                       // jmp eax
END

:_STATSWITCH
HEX
9A A7 55 00
A1 A7 55 00
A8 A7 55 00
AF A7 55 00
B6 A7 55 00
BD A7 55 00
C4 A7 55 00
CB A7 55 00
00 00 00 00
END

// Beautiful nothingness

{:Textures     // 64 bytes
HEX
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
END
:Drawings     // 64 bytes
HEX
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
 0000000000000000000000000000000000000000000000000000000000000000
END}

{}:TempMemory           // 128 bytes
05B6: 1
{}:MoreMemory           // 128 bytes
05B6: 1
{}:EvenMoreMemory       // 128 bytes
05B6: 1

// Yea, its a real VAR stack...

{}:STACK  // 128 (512 bytes)
05B6: 1
05B6: 1
05B6: 1
05B6: 1

// For easy acquirage from SCM funcs

:GLOBAL_VARIABLES_INDEX
0000:
0000:
0000:
0000: