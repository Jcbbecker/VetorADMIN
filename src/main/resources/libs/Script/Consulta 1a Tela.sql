  SELECT
    v.[Ordem],
    v.[Codigo],
    a.[Descricao],
    v.[Previsao],
    ISNULL(d.[Producao], 0) AS Producao
FROM [MassasPrev] AS v
LEFT JOIN (
    SELECT
        d.[DataPrev],
        d.[Codigo],
        SUM(d.[Producao]) AS Producao
    FROM [MassasProd] AS d
    GROUP BY d.DataPrev, d.Codigo
) AS d ON v.Dia = d.DataPrev AND v.Codigo = d.Codigo
LEFT JOIN [GPROD-Tradifana].[dbo].[Artigos] AS a ON v.Codigo = a.Artigo
WHERE v.Dia = '2024-05-21'
  AND v.Codigo IN (
    SELECT Massa
    FROM [GPROD-Tradifana].[dbo].[Ingredientes]
    WHERE Centro = 'Amassadeira'
)
ORDER BY v.Ordem;
