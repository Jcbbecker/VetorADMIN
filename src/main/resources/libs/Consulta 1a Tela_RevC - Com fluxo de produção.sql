
-- Esta consulta agrupa as Massas do MassasProd filtrado pelo Dia, Centro e considerando o Fluxo da produção

-- Previsão -> Massas previstas no MassasPrev
-- Produção -> Massas com o fluxo do centro/massa igual ou superior ao indicado nas MassasProd -1 (estado anterior)
-- Quebra -> Massas com o fluxo do centro/massa negativo (*-1)
-- Concluido -> Massas com o fluxo do centro/massa igual ou superior ao indicado nas MassasProd

DECLARE @DATA DATE = '2024-05-22';
DECLARE @CENTRO NVARCHAR(30) = 'Amassadeira';

WITH IngredientesFluxo AS ( -- Obtém o fluxo e o fluxo negativo para cada massa no centro
	SELECT 
        f.Massa, 
        f.Fluxo AS Fluxo,
        (f.Fluxo * -1) AS FluxoNegativo
    FROM FluxoMassaCentro f
    WHERE f.Centro = @CENTRO
),
ProducaoAgrupada AS ( -- Agrupa as produções
    SELECT 
        mp.Codigo, 
        SUM(mp.Producao) AS Producao
    FROM MassasProd mp
    JOIN IngredientesFluxo imf ON mp.Codigo = imf.Massa
    WHERE mp.DataPrev = @DATA
        AND (mp.Fluxo + 1 >= imf.Fluxo OR (mp.Fluxo <= imf.FluxoNegativo AND mp.Fluxo < 0))
    GROUP BY mp.Codigo
),
MassasPrevFiltradas AS (  -- Soma as previsões de cada massa filtrado pelo fluxo do Centro
    SELECT 
        v.Ordem, 
        v.Codigo, 
        v.Previsao, 
        ISNULL(pa.Producao, 0) AS Producao
    FROM MassasPrev v
    LEFT JOIN ProducaoAgrupada pa ON v.Codigo = pa.Codigo
    WHERE v.Dia = @DATA
        AND EXISTS (SELECT 1 FROM IngredientesFluxo i WHERE i.Massa = v.Codigo)
),
MassasProdNaoPrevistas AS (  -- Soma as massas produzidas sem previsão filtrado pelo fluxo do Centro e quebras
    SELECT 
        0 AS Ordem, 
        mp.Codigo, 
        0 AS Previsao, 
        SUM(mp.Producao) AS Producao
    FROM MassasProd mp
    JOIN IngredientesFluxo imf ON mp.Codigo = imf.Massa
    WHERE mp.DataPrev = @DATA
        AND NOT EXISTS (SELECT 1 FROM MassasPrev v WHERE v.Dia = @DATA AND v.Codigo = mp.Codigo)
        AND (mp.Fluxo + 1 >= imf.Fluxo OR (mp.Fluxo <= imf.FluxoNegativo AND mp.Fluxo < 0))
    GROUP BY mp.Codigo
),
MassasProdFiltradasPorFluxo AS (  -- Soma as produções de cada massa filtrado pelo fluxo do Centro e quebras
    SELECT 
        d.Codigo, 
        SUM(d.Producao) AS ProducaoFiltrada
    FROM MassasProd d
    JOIN IngredientesFluxo imf ON d.Codigo = imf.Massa
    WHERE d.DataPrev = @DATA
        AND (d.Fluxo >= imf.Fluxo OR (d.Fluxo < imf.FluxoNegativo AND d.Fluxo < 0))
    GROUP BY d.Codigo
),
QuebraCalculada AS (  -- Soma as Quebras de cada massa filtrado pelo fluxo do Centro e quebras
    SELECT 
        d.Codigo, 
        SUM(d.Producao) AS Quebra
    FROM MassasProd d
    JOIN IngredientesFluxo imf ON d.Codigo = imf.Massa
    WHERE d.DataPrev = @DATA
        AND d.Fluxo = imf.FluxoNegativo
    GROUP BY d.Codigo
)
SELECT 
    imf.Fluxo,  -- Adicionando a coluna Fluxo como primeira coluna
    cd.Ordem, 
    cd.Codigo, 
    a.Descricao, 
    cd.Previsao, 
    cd.Producao, 
    ISNULL(q.Quebra, 0) AS Quebra,
    ISNULL(mpf.ProducaoFiltrada, 0) AS Concluido
FROM (
    SELECT * FROM MassasPrevFiltradas
    UNION ALL
    SELECT * FROM MassasProdNaoPrevistas
) cd
LEFT JOIN [GPROD-Tradifana].[dbo].[Artigos] a ON cd.Codigo = a.Artigo
LEFT JOIN MassasProdFiltradasPorFluxo mpf ON cd.Codigo = mpf.Codigo
LEFT JOIN QuebraCalculada q ON cd.Codigo = q.Codigo
LEFT JOIN IngredientesFluxo imf ON cd.Codigo = imf.Massa 
ORDER BY cd.Ordem;
